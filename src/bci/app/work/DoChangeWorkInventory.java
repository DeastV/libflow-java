package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import bci.core.exception.*;
//FIXME add more imports if needed

/**
 * Change the number of exemplars of a work.
 */
class DoChangeWorkInventory extends Command<LibraryManager> {
  DoChangeWorkInventory(LibraryManager receiver) {
    super(Label.CHANGE_WORK_INVENTORY, receiver);
    addIntegerField("id", Prompt.workId());
    addIntegerField("quantity", Prompt.amountToDecrement());
  }
// Falta:No caso de nao ser poss ˜ ´ıvel
//actualizar o numero de exemplares devido ao facto de a quantidade indicada ser inv ´ alida, ent ´ ao a operac¸ ˜ ao n ˜ ao deve ter qualquer ˜
//efeito e deve ser apresentada a mensagem Message.notEnoughInventory().
 
  @Override
  protected final void execute() throws CommandException {
      int quantity = integerField("quantity");
      int id = integerField("id");

      if (!_receiver.validWork(id)) throw new NoSuchWorkException(id);

      try {
          _receiver.alterInvWork(quantity, id);
      } catch (NotEnoughInventoryExceptionCore e) {
          _display.popup(Message.notEnoughInventory(id, quantity));
      } catch (NoSuchWorkExceptionCore e) {
          throw new NoSuchWorkException(id);
      }
  }
}