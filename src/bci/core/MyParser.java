package bci.core;

import java.io.*;

import java.util.List;
import java.util.ArrayList;

import bci.core.exception.UnrecognizedEntryException;

// MAYBE more import
/**
 * Parser responsible for reading and loading data from a text file into the library system.
 * <p>
 * Each line in the file describes an entity (user, book, or DVD) according to the
 * predefined format specified in the project statement. Invalid entries result in
 * an {@code UnrecognizedEntryException}.
 */
class MyParser implements Serializable {
  private Library _library;

  /**
   * Creates a parser associated with the given library instance.
   *
   * @param lib the library where the parsed data will be stored
   */
  MyParser(Library lib) {
    _library = lib;
  }

  /**
   * Parses the specified file and loads its contents into the library.
   *
   * @param filename the name of the file to read
   * @throws IOException if an I/O error occurs or an invalid entry is found
   */
  void parseFile(String filename) throws IOException {
    String line;

    try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
      while ((line = in.readLine()) != null) {
        parseLine(line);
      }
    } catch (UnrecognizedEntryException e) {
      throw new IOException();
    }
  }

  /**
   * Processes a single line from the input file, going to the appropriate
   * parsing method based on the entity type.
   *
   * @param line the line to parse
   * @throws UnrecognizedEntryException if the entry type is unknown or invalid
   */
  private void parseLine(String line) throws UnrecognizedEntryException {
    String[] components = line.split(":");

    switch (components[0]) {
      case "USER":
        parseUser(components, line);
        break;

      case "DVD":
        parseDvd(components, line);
        break;

      case "BOOK":
        parseBook(components, line);
        break;

      default:
        throw new UnrecognizedEntryException(
          "Invalid type " + components[0] + " in line " + line
        );
    }
  }

  /**
   * Parses and registers a user entry.
   *
   * @param components the line split into fields
   * @param line the original line content (for error reporting)
   * @throws UnrecognizedEntryException if the number of fields is incorrect
   */
  private void parseUser(String[] components, String line)
      throws UnrecognizedEntryException {
    if (components.length != 3)
      throw new UnrecognizedEntryException(
          "Invalid number of fields (3) in user description: " + line);

    _library.registerUser(components[1], components[2]);
  }

  /**
   * Parses and registers a DVD entry.
   *
   * @param components the line split into fields
   * @param line the original line content (for error reporting)
   * @throws UnrecognizedEntryException if the number of fields is incorrect
   */
  private void parseDvd(String[] components, String line)
      throws UnrecognizedEntryException {
    if (components.length != 7)
      throw new UnrecognizedEntryException(
          "Invalid number of fields (7) in DVD description: " + line);

    int price = Integer.parseInt(components[3]);
    int nCopies = Integer.parseInt(components[6]);
    Category category = Category.valueOf(components[4]);
    Creator creator = _library.registerCreator(components[2].trim());

    _library.registerDvd(components[5], creator, components[1], price, nCopies, category);
  }

  /**
   * Parses and registers a book entry.
   *
   * @param components the line split into fields
   * @param line the original line content (for error reporting)
   * @throws UnrecognizedEntryException if the number of fields or ISBN format is invalid
   */
  private void parseBook(String[] components, String line)
      throws UnrecognizedEntryException {
    if (components.length != 7 ||
        (components[5].length() != 10 && components[5].length() != 13))
      throw new UnrecognizedEntryException(
           "Invalid number of fields (7) or ISBN format in book description: " + line);

    int price = Integer.parseInt(components[3]);
    int nCopies = Integer.parseInt(components[6]);
    Category category = Category.valueOf(components[4]);
    List<Creator> creators = new ArrayList<>();
    for (String name : components[2].split(","))
      creators.add(_library.registerCreator(name.trim()));

    _library.registerBook(components[5], price, components[1], nCopies, creators, category);
  }
}
