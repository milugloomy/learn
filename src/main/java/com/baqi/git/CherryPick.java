package com.baqi.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/***
 *      ┌─┐       ┌─┐
 *   ┌──┘ ┴───────┘ ┴──┐
 *   │                 │
 *   │       ───       │
 *   │  ─┬┘       └┬─  │
 *   │                 │
 *   │       ─┴─       │
 *   │                 │
 *   └───┐         ┌───┘
 *       │         │
 *       │         │
 *       │         │
 *       │         └──────────────┐
 *       │                        │
 *       │                        ├─┐
 *       │                        ┌─┘
 *       │                        │
 *       └─┐  ┐  ┌───────┬──┐  ┌──┘
 *         │ ─┤ ─┤       │ ─┤ ─┤
 *         └──┴──┘       └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
public class CherryPick {

    public static void main(String[] args) throws IOException {
        String repoPath = Path.patientWeb;
        String comment = "12313213213";
        CherryPick cherryPick = new CherryPick(repoPath, comment);
        cherryPick.execute();
    }

    private Git git;
    private String comment;
    // CherryPick 的提交数, 默认1
    private Integer cherryPickCount = 1;

    public CherryPick(String repoPath, String comment) throws IOException {
        this.comment = comment;
        git = Git.open(new File(repoPath));
    }

    public void execute() {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(Constants.username, Constants.password);

        try {
            // 切换到dev分支
            git.checkout().setCreateBranch(false).setName("dev").call();
            git.pull().setCredentialsProvider(cp).call();

            // 提交代码
            git.add().addFilepattern(".").call();
            git.commit().setMessage(comment).call();
            git.push().setCredentialsProvider(cp).call();

            // 查找最近提交中我提交的记录
            Iterable<RevCommit> commitLog = git.log().setRevFilter(new RevFilter() {
                @Override
                public boolean include(RevWalk walker, RevCommit cmit) throws StopWalkException, MissingObjectException, IncorrectObjectTypeException, IOException {
                    return cmit.getAuthorIdent().getEmailAddress().equals(Constants.username);
                }

                @Override
                public RevFilter clone() {
                    return this;
                }
            }).call();

            // 切换到test分支
            git.checkout().setCreateBranch(false).setName("test").call();
            git.pull().setCredentialsProvider(cp).call();

            // cherrypick
            int index = 0;
            Iterator<RevCommit> commitIterator = commitLog.iterator();
            while (commitIterator.hasNext()) {
                RevCommit commit = commitIterator.next();
                // 大于1的是分支合并  找不在test分支上的提交
                if (commit.getParentCount() == 1 && findInBranch(commit, "test") == false) {
                    git.cherryPick().include(commit).call();
                    // 大于最大次数 break
                    if (++index > cherryPickCount) {
                        break;
                    }
                }
            }

            // 不需要commit，直接push
            git.push().setCredentialsProvider(cp).call();

            // 切换到dev分支
            git.checkout().setCreateBranch(false).setName("dev").call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } finally {
            git.close();
        }
    }

    // 某次提交是否在一个分支上
    private boolean findInBranch(RevCommit commit, String branchName) throws GitAPIException {
        List<Ref> list = git.branchList().setContains(commit.name()).call();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().endsWith(branchName)) {
                return true;
            }
        }
        return false;
    }

    public void setCherryPickCount(Integer cherryPickCount) {
        this.cherryPickCount = cherryPickCount;
    }
}
