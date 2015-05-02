
/*
* */

package com.yao;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 5/2/15.
 *
 * @goal copyright
 *
 *
 * @execute phase=package
 */
public class CopyRight extends AbstractMojo {
    private static final String  JAVA_SUFFIX=".java";
    private static final String  COPY_RIGHT="\n"+
                                            "/** CopyRight \n" +
                                            "* xxxxxxxxxxxxxxxxx Robin\n" +
                                            "* xxxxxxxxxxxxxxxxx Robin\n" +
                                            "* END \n"+
                                            "*/\n";
    /**
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     * @readonly
     */
    private File sourcedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        addCopyRight(sourcedir);
    }
    private void addCopyRight(File dir){
        if(!dir.isDirectory()){
            getLog().error(new IllegalArgumentException("源代码目录不对!"));
        }
        List<File> javaFiles=new ArrayList<File>();
        findJavaFiles(dir,javaFiles);
        if(javaFiles.size()>0){
           for (File file:javaFiles){
               addCopyHead(file);
           }
        }
    }
    private void findJavaFiles(File file,List<File> javaFiles){
        getLog().info("scan file:"+file.getAbsolutePath());
        if(file.isDirectory()){
            for (File child:file.listFiles()){
                findJavaFiles(child,javaFiles);
            }
        }else {
           if(file.getName().endsWith(JAVA_SUFFIX)){
              javaFiles.add(file);
           }
        }
    }
    private void addCopyHead(File file){
        if(file.isDirectory()){
            getLog().error(new IllegalArgumentException("要添加版权头的必须是文件！"));
        }
        try(RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
            RandomAccessFile tmpFile=new RandomAccessFile(File.createTempFile("copy_right","tmp"),"rw")){
            tmpFile.writeBytes(new String(COPY_RIGHT.getBytes("UTF-8")));
            randomAccessFile.seek(0);
            String line=null;
            while((line=randomAccessFile.readLine())!=null){
                tmpFile.writeBytes(new String(line.getBytes("UTF-8"))+"\n");
            }
            randomAccessFile.setLength(0);
            FileChannel in=tmpFile.getChannel();
            FileChannel out=randomAccessFile.getChannel();
            in.transferTo(0,tmpFile.length(),out);
        }catch (Exception e){
            getLog().error(e);
        }
    }
    public static void main(String[]args) throws IOException {
       /* try(RandomAccessFile randomAccessFile=new RandomAccessFile("/home/robin/tmp/blogtmp.txt","rw")){
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeChars(COPY_RIGHT);
        }catch (Exception e){
        }*/
    }
}

