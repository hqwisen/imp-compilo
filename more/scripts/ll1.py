#!/bin/python3

from config import V, T

import logging

log = logging.getLogger(__name__)

class GrammarException(Exception):
    pass

GRAMMAR_CONFIG = "grammar.py"

def get_grammar_config():
    try:
        config = {}
        exec(open(GRAMMAR_CONFIG).read(), config)
        # FIXME find another way to parse to avoid del builtins
        del config['__builtins__']
        return config
    except FileNotFoundError:
        print("Error: no '%s' file found." % GRAMMAR_CONFIG)
        exit(1)
    except Exception as e:
        log.error("Config Error: ", e)
        exit(1)

class GrammarParser:

    @classmethod
    def _add_in(cls, collection, element):
        for e in collection:
            if element.same(e):
                return;
        log.debug('Adding %s %s' % (element.__class__.__name__, element))
        collection.append(element)


    def __init__(self, grammar, epsilon):
        self.grammar = grammar
        self.epsilon = epsilon
        self.variables = []
        self.terminals = []


    def print_rules(self):
        i = 1
        prleft = ""
        for left in self.grammar:
            right = str()
            for elem in self.grammar[left]:
                right += str(elem) + " "
            nleft = left if str(left) != prleft else ""
            print('{: >5} {: >20} → {: <50}'.format(str(i), str(nleft), right))
            i+=1
            prleft = str(left)

    def add_variable(self, variable):
        """
        Ensure that a variable is added only once
        """
        GrammarParser._add_in(self.variables, variable)

    def add_terminal(self, terminal):
        """
        Ensure that a terminal is added only once
        """
        GrammarParser._add_in(self.terminals, terminal)

    def parse(self):
        for left in self.grammar:
            # leftside is always a var
            self.add_variable(left)
            for elem in self.grammar[left]:
                if isinstance(elem, V):
                    self.add_variable(elem)
                elif isinstance(elem, T):
                    self.add_terminal(elem)
                elif elem == self.epsilon:
                    pass # If epsilon, there is nothing to do
                else:
                    raise GrammarException("Element is not a terminal, " \
                                            "a variable or epsilon, instead " \
                                            "'%s\'" % elem.__class__)
        return {'variables': self.variables, 'terminals': self.terminals}





if __name__ == "__main__":
    config = get_grammar_config()
    parser = GrammarParser(config['grammar'], config['epsilon'])
    print()
    parser.print_rules()
    print()
    data = parser.parse()
    print(data)
