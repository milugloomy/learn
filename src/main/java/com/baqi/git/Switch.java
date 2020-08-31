package com.baqi.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Switch {

    static String Branch_Dev = "dev";
    static String Branch_Test = "test";
    static String Branch_Release = "release";

    public static void main(String[] args) throws IOException, GitAPIException, IllegalAccessException {
        Switch switch2 = new Switch();
//        String[] selected = new String[]{
////                Path.patientWeb,
////                Path.doctorWeb,
//                Path.heartPatient,
//                Path.heartDoctor,
//                Path.tumourPatient,
//                Path.tumourDoctor,
//                Path.miniHospital,
////                Path.ms,
//        };
//        switch2.switchSelected(Arrays.asList(selected), Branch_Test);
//        switch2.switchAll(Branch_Dev);
        Git git = Git.open(new File(Path.patientWeb));
        Status status = git.status().call();
        Set<String> changed = status.getModified();
        System.out.println(changed);
        List<DiffEntry> diff = git.diff().setSourcePrefix("src/App.vue").call();
        DiffEntry diffEntry = diff.get(0);
        System.out.println(diffEntry);
    }

    public void switchSelected(List<String> selected, String branch) throws IOException, GitAPIException {
        switchBase(branch, selected);
    }

    public void switchAll(String branch) throws IllegalAccessException, IOException, GitAPIException {
        Class pathClass = Path.class;
        Field[] fields = pathClass.getFields();
        List<String> pathList = new ArrayList<>();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                pathList.add((String) field.get(pathClass));
            }
        }
        switchBase(branch, pathList);
    }

    public void switchBase(String branch, List<String> pathList) throws IOException, GitAPIException {
        CredentialsProvider cp = new UsernamePasswordCredentialsProvider(Constants.username, Constants.password);
        for (String path: pathList) {
            Git git = Git.open(new File(path));
            git.pull().setCredentialsProvider(cp).call();
            git.checkout().setCreateBranch(false).setName(branch).call();
            git.pull().setCredentialsProvider(cp).call();
        }
    }
}
