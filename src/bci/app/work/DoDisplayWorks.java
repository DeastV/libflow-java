package bci.app.work;

import bci.core.LibraryManager;
import bci.core.Work;
import pt.tecnico.uilib.menus.Command;

import java.util.Comparator;
import java.util.List;

/**
 * Command to display all works.
 */
class DoDisplayWorks extends Command<LibraryManager> {

  DoDisplayWorks(LibraryManager receiver) {
    super(Label.SHOW_WORKS, receiver);
  }

  @Override
  protected final void execute() {
      List<Work> works = _receiver.listWorks();
      works.sort(Comparator.comparingInt(Work::getWorkId));

      StringBuilder sb = new StringBuilder();
      for (Work w : works)
          sb.append(w.toString()).append("\n");
      _display.popup(sb.toString());
  }
}
