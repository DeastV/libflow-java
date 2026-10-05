package bci.core;

import java.util.ArrayList;
import java.util.List;

class Book extends Work{
    private String _isbn;
    private List<Creator> _creators; 

    Book(String isbn, int price, String title, int numberOfCopies, List<Creator> creators, Category type, int nextWorkId) {
        super(title, price, numberOfCopies, type, nextWorkId);
        _isbn = isbn;

        _creators = new ArrayList<>(creators);
    }

    @Override
    String getType() {
        return "Livro";
    }

    @Override
    String getAdInfo() {
        List<String> creatorNames = new ArrayList<>();
        for (Creator c : _creators) {
            creatorNames.add(c.getName());
        }
        return String.join(";", creatorNames) + " - " + _isbn;
    }

    @Override
    List<Creator> listCreators() {
        return List.copyOf(_creators);
    }

    String getIsbn(){return _isbn;}
}
