package edu.yu.parallel;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelFileSearch implements FileSearch {

    ConcurrentHashMap<File, Integer> dirToIndex = new ConcurrentHashMap<>();
    //ConcurrentLinkedQueue<Thread> tPool = new ConcurrentLinkedQueue<>();
    private static volatile AtomicInteger threadsAvailable = new AtomicInteger(Runtime.getRuntime().availableProcessors());

    int fileCounter;
    @Override
    public void searchFiles(File initialPath, String searchName, Result result) {
        if(initialPath.exists()){
            //If the given dir is a valid absolute or relative path then we found it
            System.out.print("thing exists and it is");
            if(initialPath.isDirectory()){
                System.out.print(" IS a directory!");
                System.out.println(initialPath.getName());
                // System.out.println(initialPath.getParentFile().getName());
                //result.setPath("can't be found");
                //iterSearch(initialPath,searchName, result);
                //recSearch(initialPath,searchName, result);
                dirToIndex.putIfAbsent(initialPath, 0);
                Thread t = new dirSearcher(initialPath,searchName,result);
                t.start();

            }
            else {
                System.out.println(" NOT a directory");
                result.setPath("can’t be found");
            }

        }
        else {
            System.out.println("Query Could not be found!");
        }
    }
    private void iterSearch(File directoryToSearch, String searchName, Result result){
        File currentDir = directoryToSearch;
        File rootOfRoot = currentDir.getParentFile();
        //String rootOfRoot = directoryToSearch.getParentFile().getName();
        while(!currentDir.equals(rootOfRoot) && !result.isFound()){
            File[] contents = currentDir.listFiles();//assumption has dirs and files
            dirToIndex.putIfAbsent(currentDir, 0);
            for (int i = dirToIndex.get(currentDir); i < contents.length; i = dirToIndex.get(currentDir)) {//0 < 0
                fileCounter++;
                if (contents[i].isFile()) {

                    if (contents[i].getName().equals(searchName)) {
                        result.setPath(contents[i].getAbsolutePath());
                        break;
                    }
                    dirToIndex.put(currentDir, i + 1);
                } else if(!contents[i].isHidden()){//if its not a hidden directory go in it
                    //if no thread available do it yourself, otherwise hand task to thread
                    dirToIndex.put(currentDir, i + 1);//TODO: Off by one error?
                    currentDir = contents[i];
                    dirToIndex.putIfAbsent(currentDir, 0);
                    contents = currentDir.listFiles();//you can use this to skip a step i think
                    if(contents == null) {
                        System.out.println(currentDir);
                        currentDir = currentDir.getParentFile();//reverse
                        contents = currentDir.listFiles();//reverse
                    }
                }
                else{
                    //skip over
                    dirToIndex.put(currentDir, i + 1);
                }
            }
            //reached end of list/that dir contents -- go up
            currentDir = currentDir.getParentFile();
        }
        if (!result.isFound()){
            result.setPath("can't be found");
        }
        //System.out.println(dirToIndex.size());
        System.out.println(fileCounter);
    }

    private class dirSearcher extends Thread{
        private File currentDir;
        private File[] contents;
        private Result result;
        private String searchName;
        private File rootOfRoot;
        public dirSearcher(File dir, String searchName, Result res){
            this.currentDir = dir;
            this.contents = currentDir.listFiles();
            this.result = res;
            this.searchName = searchName;
            this.rootOfRoot = currentDir.getParentFile();
        }
        @Override
        public void run(){
            while(!currentDir.equals(rootOfRoot) && !result.isFound()) {
                File[] contents = currentDir.listFiles();//assumption has dirs and files
                dirToIndex.putIfAbsent(currentDir, 0);
                for (int i = dirToIndex.get(currentDir); i < contents.length && !result.isFound(); i = dirToIndex.get(currentDir)) {//0 < 0
                    fileCounter++;
                    if (contents[i].isFile()) {
                        if (contents[i].getName().equals(searchName)) {
                            result.setPath(contents[i].getAbsolutePath());
                            System.out.println("FOUND!!!! " + result.getPath());
                            break;
                        }
                        dirToIndex.put(currentDir, i + 1);
                    } else if (!contents[i].isHidden() && contents[i].listFiles() != null) {//if its not a hidden directory go in it
                        //if no thread available do it yourself, otherwise hand task to thread// 1- determine if kosher dir 2- thread available?
                        dirToIndex.put(currentDir, i + 1);//TODO: Off by one error?
                        if (threadsAvailable.get() > 0) {
                            new dirSearcher(contents[i], searchName, result).start();//we give it a dir and dont check if its retarded
                            threadsAvailable.getAndDecrement();
                        } else {
                            currentDir = contents[i];
                            dirToIndex.putIfAbsent(currentDir, 0);
                            contents = currentDir.listFiles();//you can use this to skip a step i think
                            if (contents == null) {
                                System.out.println(currentDir);
                                currentDir = currentDir.getParentFile();//reverse
                                contents = currentDir.listFiles();//reverse
                            }
                        }
                    } else {
                        //skip over
                        dirToIndex.put(currentDir, i + 1);
                    }
                }
                currentDir = currentDir.getParentFile();
                threadsAvailable.getAndIncrement();
            }
            }
    }
}
