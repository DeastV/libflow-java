package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.core.exception.*;
//FIXME add more imports if needed

/**
 * 4.2.3. Show notifications of a specific user.
 */
class DoShowUserNotifications extends Command<LibraryManager> {

  DoShowUserNotifications(LibraryManager receiver) {
    super(Label.SHOW_USER_NOTIFICATIONS, receiver);
    addIntegerField("userId", Prompt.userId());
  }

  @Override
  protected final void execute() throws CommandException{
    int id = integerField("userId");
    try {
      _display.popup(_receiver.showUserNotifications(id));
    } catch (NoSuchUserExceptionCore e) {
      throw new NoSuchUserException(id);
    }
  }
}