package main.java;

import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.*;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.parsing.ast.*;
import java.util.*;
import java.util.stream.Collectors;

public class aufgabe1 {

    public static void main(final String[] args) {
        FOLKnowledgeBase KB = inputKnowledgeBase(new FOLFCAsk());
        inputQuery(KB);
    }

    public static FOLDomain inputDomain() {
        System.out.println("\033[0;33m Press Enter to switch to next Category\033[0m");
        Scanner scanner = new Scanner(System.in);
        FOLDomain domain = new FOLDomain();
        String[] inputTypes = {"Constant", "Predicate"};
        Arrays.stream(inputTypes).forEach((String type) -> {
            System.out.print("Add " + type + ": ");
            String input = "";
            while( !(input = scanner.nextLine()).equals("")) {
                System.out.print("Add " + type + ": ");
                switch(type) {
                    case "Constant":
                        domain.addConstant(input);
                        break;
                    case "Predicate":
                        domain.addPredicate(input);
                        break;
                }
            }
        });
        return domain;
    }

    public static FOLKnowledgeBase inputKnowledgeBase(InferenceProcedure infp) {
        FOLKnowledgeBase kb = new FOLKnowledgeBase(inputDomain(), infp);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Add knowledge base: ");
        String input = "";
        while(!(input = scanner.nextLine()).equals("")) {
            try {
                kb.tell(input);
            } catch (RuntimeException e) {
                System.out.println("\033[0;31mInvalid knowledge base\033[0m");
            }
            System.out.print("Add knowledge base: ");
        }
        return kb;
    }

    public static void inputQuery(FOLKnowledgeBase KB) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Search for query: ");
        String input = "";
        while(!(input = scanner.nextLine()).equals("")) {
            try {
                String[] arguments = input.substring(input.indexOf('(') + 1, input.indexOf(')')).split(",");
                List<Term> terms = Arrays.stream(arguments)
                        .map(x -> KB.domain.getConstants().contains(x) ? new Constant(x) : new Variable("x"))
                        .collect(Collectors.toList());
                Predicate query = new Predicate(input.split("\\(")[0], terms);
                InferenceResult answer = KB.ask(query);
                if(answer.isTrue()) {
                    System.out.println("\033[0;32mStatement is true\033[0m");
                    answer.getProofs().stream().filter(x -> !x.getAnswerBindings().isEmpty()).forEach(System.out::println);
                } else {
                    System.out.println("\033[0;31mStatement is wrong or no solutions are found\033[0m");
                }
            } catch(Exception e) {
                System.out.println("\033[0;31mInvalid Query\033[0m");
            }
            System.out.print("Search for query: ");
        }
    }
}
