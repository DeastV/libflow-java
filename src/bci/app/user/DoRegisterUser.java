package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.UserRegistrationFailedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.1. Register new user.
 */
class DoRegisterUser extends Command<LibraryManager> {

  DoRegisterUser(LibraryManager receiver) {
    super(Label.REGISTER_USER, receiver);
    addStringField("name", Prompt.userName());
    addStringField("email", Prompt.userEMail());
  }

  @Override
  protected final void execute() throws CommandException {
    String name = stringField("name");
    String email = stringField("email");
    if(name == null || email == null || name.isBlank() || email.isBlank())
      throw new UserRegistrationFailedException(name, email);
    _display.popup(Message.registrationSuccessful(_receiver.registerUser(name, email)));
  }
}

