#!/bin/bash
mvn compile
mvn exec:java -Dexec.mainClass="fr.iut45.Main"

# Permet de convertir un fichier .dot en pdf
# dot -Tpdf graph.dot -o graph.pdf
