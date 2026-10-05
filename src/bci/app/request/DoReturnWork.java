package bci.app.request;

import bci.app.exception.UserIsActiveException;
import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.app.exception.WorkNotBorrowedByUserException;
import bci.core.User;
import bci.core.exception.UserIsActiveExceptionCore;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.4.2. Return a work.
 */
class DoReturnWork extends Command<LibraryManager> {

  DoReturnWork(LibraryManager receiver) {
    super(Label.RETURN_WORK, receiver);
    addIntegerField("userID", bci.app.user.Prompt.userId());
    addIntegerField("workID", bci.app.work.Prompt.workId());
  }

  @Override
  protected final void execute() throws CommandException {
      int userId = integerField("userID");
      int workId = integerField("workID");

      if (!_receiver.validUser(userId)) throw new NoSuchUserException(userId);
      if (!_receiver.validWork(workId)) throw new NoSuchWorkException(workId);

      int fine = _receiver.returnWork(userId, workId);

      if (fine < 0) //o valor -1 indica que ocorreu um erro na remocao
          throw new WorkNotBorrowedByUserException(workId, userId);
      else
          if(fine > 0) {
              _display.popup(Message.showFine(userId, fine));
              if (Form.confirm(Prompt.finePaymentChoice()))
                  try {
                      _receiver.payFine(userId);
                  } catch(UserIsActiveExceptionCore e){
                      throw new UserIsActiveException(userId);
                  }
              else _receiver.setFine(userId, fine);
          }

  }
}
