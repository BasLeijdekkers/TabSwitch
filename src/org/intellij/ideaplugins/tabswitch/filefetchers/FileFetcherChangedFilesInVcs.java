package org.intellij.ideaplugins.tabswitch.filefetchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Creates a list of {@link VirtualFile} by fetching all the modified files that are in the default change list of
 * chosen VCS.
 * <pre>
 * User: must
 * Date: 2012-06-02
 * </pre>
 */
public class FileFetcherChangedFilesInVcs implements FileFetcher<VirtualFile> {

  private static final Comparator<VirtualFile> VIRTUAL_FILE_NAME_COMPARATOR = new VirtualFileNameComparator();

  /**
   * @param project an idea project.
   *
   * @return Not {@code null}. Alphabetically sorted list of modified files.
   */
  @Override
  public List<VirtualFile> getFiles(final Project project) {
    final List<VirtualFile> files = new ArrayList<VirtualFile>();
    final Collection<Change> changes = getChanges(project);
    if (changes != null) {
      final int editorTabLimit = UISettings.getInstance().EDITOR_TAB_LIMIT;
      int i = 0;
      for (Change change : changes) {
        final VirtualFile virtualFile = change.getVirtualFile();
        if (virtualFile != null) {
          files.add(virtualFile);
          if (i++ == editorTabLimit) break;
        }
      }
      Collections.sort(files, VIRTUAL_FILE_NAME_COMPARATOR);
    }
    return files;
  }

  /**
   * @param project an idea project.
   *
   * @return {@code null} if no change list is available or if there are no changes currently made.
   */
  private Collection<Change> getChanges(final Project project) {
    LocalChangeList defaultChangeList = ChangeListManager.getInstance(project).getDefaultChangeList();
    return defaultChangeList != null ? defaultChangeList.getChanges() : null;
  }

  private static final class VirtualFileNameComparator implements Comparator<VirtualFile> {

    @Override
    public int compare(final VirtualFile vf1, final VirtualFile vf2) {
      return vf1.getName().compareToIgnoreCase(vf2.getName());
    }
  }
}
