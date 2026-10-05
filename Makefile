JC = javac
JFLAGS = -g -encoding UTF-8
SRCDIR = src
BINDIR = bin

MAIN_CLASS = bci.app.App

SOURCES := $(shell find $(SRCDIR) -name "*.java")

all: compile

compile:
	@mkdir -p $(BINDIR)
	@echo "Compiling Java sources..."
	@$(JC) $(JFLAGS) -d $(BINDIR) $(SOURCES)
	@echo "Compilation successful. Output located in $(BINDIR)/"

run: compile
	@java -cp $(BINDIR) $(MAIN_CLASS)

clean:
	@rm -rf $(BINDIR)
	@echo "Cleaned build artifacts."

.PHONY: all compile run clean
