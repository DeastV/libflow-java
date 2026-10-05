package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.core.exception.NoSuchUserExceptionCore;

/**
 * 4.2.2. Show specific user.
 */
class DoShowUser extends Command<LibraryManager> {

  DoShowUser(LibraryManager receiver) {
    super(Label.SHOW_USER, receiver);
    addIntegerField("id", Prompt.userId());
  }

  @Override
  protected final void execute() throws CommandException {
    int id = integerField("id");
    try {
      _display.popup(_receiver.listUser(id).toString());
    } catch (NoSuchUserExceptionCore e) {
      throw new NoSuchUserException(id);
    }
  }
}
