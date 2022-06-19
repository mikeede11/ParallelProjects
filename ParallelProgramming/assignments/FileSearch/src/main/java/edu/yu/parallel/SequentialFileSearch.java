package edu.yu.parallel;

import java.io.File;
import java.util.HashMap;

public class SequentialFileSearch implements FileSearch {

    HashMap<File, Integer> dirToIndex = new HashMap<>();
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
                iterSearch(initialPath,searchName, result);
                //recSearch(initialPath,searchName, result);
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
    private void recSearch(File directoryToSearch, String searchName, Result result){

        File[] contents = directoryToSearch.listFiles();
        for(File f: contents) {
            //File f = new File(path);
            if (f.isFile()){
                if(f.getName().equals(searchName)) {
                    result.setPath(f.getAbsolutePath());
                    return;//return;
                }
            }
            else{
                //must be a directory
                if(f.listFiles() == null){
                    continue;
                }
                recSearch(f,searchName,result);
            }
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
        System.out.println(dirToIndex.size());
        System.out.println(fileCounter);
    }
    public static void main(String[] args){
        SequentialFileSearch sfs = new SequentialFileSearch();
        System.out.println(Runtime.getRuntime().availableProcessors());
        ParallelFileSearch pfs = new ParallelFileSearch();
        Result result = new Result();
        //sfs.searchFiles(new File("C:\\"), "File2.txt", result);
        pfs.searchFiles(new File("C:\\"), "File2.txt", result);
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println(result.getPath());
    }

    /*if the parent directory dirtoindex(parentdir/currentafterswitch) == contents.size then we know this was finished. maybe double work but at least find so just go back up*/
}
