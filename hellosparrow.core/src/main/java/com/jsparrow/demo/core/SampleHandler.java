package com.jsparrow.demo.core;

import java.io.File;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/** Executed by click menu.<br> */
public class SampleHandler extends AbstractHandler {

  private final IWorkbenchWindow window;

  /** constructor. */
  public SampleHandler() {
    IWorkbench workbench = PlatformUI.getWorkbench();
    this.window = workbench.getActiveWorkbenchWindow();
  }

  /** {@inheritDoc} */
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    File descriptionFile =
        Paths.get(System.getProperty("user.home"))
            .resolve("git")
            .resolve("sparrow-test")
            .resolve(".project")
            .toFile();

    MessageDialog.openInformation(
        window.getShell(),
        "Eclipse Plugin Archetype",
        "Hello, Maven+Eclipse world: "
            + new ClassFinder().execute(descriptionFile.getAbsolutePath()));
    return null;
  }
}
