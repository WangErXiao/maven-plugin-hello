package com.yao;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 *
 * Created by robin on 5/2/15.
 *
 * @goal hello
 *
 */
public class HelloMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("hello maven plugin");
    }
}
