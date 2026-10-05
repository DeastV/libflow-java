package bci.app.user;

import bci.core.LibraryManager;
import bci.core.User;
import pt.tecnico.uilib.menus.Command;
import java.util.List;

import java.util.Comparator;

/**
 * 4.2.4. Show all users.
 */
class DoShowUsers extends Command<LibraryManager> {

  DoShowUsers(LibraryManager receiver) {
    super(Label.SHOW_USERS, receiver);
  }

  @Override
  protected final void execute() {
      List<User> users = _receiver.listUsers();
      users.sort(Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER).thenComparingInt(User::getUserId));

      StringBuilder sb = new StringBuilder();
      for (User u : users)
          sb.append(u.toString()).append("\n");
      _display.popup(sb.toString());
  }
}
