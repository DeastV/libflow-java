package bci.app.work;

import bci.core.LibraryManager;
import bci.core.exception.NoSuchWorkExceptionCore;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to display a work.
 */
class DoDisplayWork extends Command<LibraryManager> {

  DoDisplayWork(LibraryManager receiver) {
    super(Label.SHOW_WORK, receiver);
    addIntegerField("id", Prompt.workId());
  }

  @Override
  protected final void execute() throws CommandException {
    int id = integerField("id");
    try {
      _display.popup(_receiver.listWork(id));
    } catch (NoSuchWorkExceptionCore e) {
      throw new NoSuchWorkException(id);
    }
      
  }
}
