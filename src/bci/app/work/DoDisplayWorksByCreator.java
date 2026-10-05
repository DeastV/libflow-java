package bci.app.work;

import bci.core.LibraryManager;
import bci.core.Work;
import bci.core.exception.NoSuchCreatorExceptionCore;
import bci.app.exception.NoSuchCreatorException;
import pt.tecnico.uilib.menus.Command;

import java.util.Comparator;
import java.util.List;

/**
 * Display all works by a specific creator.
 */
class DoDisplayWorksByCreator extends Command<LibraryManager> {
  DoDisplayWorksByCreator(LibraryManager receiver) {
    super(Label.SHOW_WORKS_BY_CREATOR, receiver);
    addStringField("name", Prompt.creatorId());
  }

  @Override
  protected final void execute() throws NoSuchCreatorException {
    String name = stringField("name");
    try {
      List<Work> works = _receiver.listWorksByCreators(name);
      works.sort(Comparator.comparing(w -> w.getTitle().toLowerCase()));

      StringBuilder sb = new StringBuilder();
      for (Work w : works)
          sb.append(w).append("\n");
      _display.popup(sb.toString());
    } catch (NoSuchCreatorExceptionCore e) {
      throw new NoSuchCreatorException(name);
    }
    
  }
}
