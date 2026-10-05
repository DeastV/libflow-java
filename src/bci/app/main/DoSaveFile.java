package bci.app.main;

import bci.app.exception.FileOpenFailedException;
import bci.core.LibraryManager;
import bci.core.exception.MissingFileAssociationException;
import bci.core.exception.UnavailableFileException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

import java.io.IOException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<LibraryManager> {

    DoSaveFile(LibraryManager receiver) {
        super(Label.SAVE_FILE, receiver);
    }

    @Override
    protected final void execute() throws FileOpenFailedException {
        try {
            _receiver.save();
        } catch (UnavailableFileException e) {
            addStringField("fileName", Prompt.newSaveAs());
            String filename = stringField("fileName");

            try {
                _receiver.saveAs(filename);
            } catch (MissingFileAssociationException mfaEx) {
                _display.popup("Erro: associação de ficheiro em falta");
            } catch (IOException ioEx) {
                _display.popup("Erro ao guardar a biblioteca");
            } catch(UnavailableFileException er){
                throw new FileOpenFailedException(er);
            }
        } catch (IOException e) {
            _display.popup("Erro ao guardar a biblioteca");
        }
    }
}

  