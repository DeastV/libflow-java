package bci.app.user;

import bci.core.LibraryManager;
import bci.core.exception.UserIsActiveExceptionCore;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.UserIsActiveException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * 4.2.5. Settle a fine.
 */
class DoPayFine extends Command<LibraryManager> {

  DoPayFine(LibraryManager receiver) {
    super(Label.PAY_FINE, receiver);
    addIntegerField("id", Prompt.userId());
  }

  @Override
  protected final void execute() throws CommandException {
      int id = integerField("id");

      if(!_receiver.validUser(id)) throw new NoSuchUserException(id);
      try {
        _receiver.payFine(id);
      } catch (UserIsActiveExceptionCore e) {
        throw new UserIsActiveException(id);
      }
  }
}
