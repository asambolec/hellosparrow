package com.jsparrow.demo.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/** Executed by click menu.<br> */
public class ClassFinder {

  public String execute() {

    IWorkspace workspace = ResourcesPlugin.getWorkspace();

    File descriptionFile =
        Paths.get(System.getProperty("user.home"))
            .resolve("git")
            .resolve("sparrow-test")
            .resolve(".project")
            .toFile();

    String descriptionFilePathValue = descriptionFile.getAbsolutePath();
    Path descriptionFilePath = new Path(descriptionFilePathValue);
    IProjectDescription description;
    String classes = "";
    try {
      description = workspace.loadProjectDescription(descriptionFilePath);
      IProject project = workspace.getRoot().getProject(description.getName());
      if (!project.exists()) {
        project.create(description, new NullProgressMonitor());
      }
      if (!project.isOpen()) {
        project.open(new NullProgressMonitor());
      }

      // create IJavaProject from IProject
      IJavaProject javaProject = JavaCore.create(project);

      javaProject.open(new NullProgressMonitor());
      classes =
          Stream.of(getPackageFragments(javaProject))
              .filter(p -> isSourceJavaSource(p))
              .map(this::getCompilationunits)
              .flatMap(Stream::of)
              .map(ICompilationUnit::getElementName)
              .collect(Collectors.joining(","));
    } catch (CoreException e) {
      e.printStackTrace();
    }

    return classes;
  }

  private IPackageFragment[] getPackageFragments(IJavaProject javaProject) {
    try {
      return javaProject.getPackageFragments();
    } catch (JavaModelException e) {
      e.printStackTrace();
      return new IPackageFragment[0];
    }
  }

  private ICompilationUnit[] getCompilationunits(IPackageFragment packageFragment) {
    try {
      return packageFragment.getCompilationUnits();
    } catch (JavaModelException e) {
      e.printStackTrace();
      return new ICompilationUnit[0];
    }
  }

  private boolean isSourceJavaSource(IPackageFragment p) {
    try {
      return p.getKind() == IPackageFragmentRoot.K_SOURCE;
    } catch (JavaModelException e) {
      e.printStackTrace();
      return false;
    }
  }
}
