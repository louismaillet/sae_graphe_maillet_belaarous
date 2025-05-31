package fr.iut45;

import com.google.gson.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String chemin = "data_100.txt";

        Set<String> acteurs = getListeActeur(chemin);
        Map<String, Set<String>> dico = dicoActeur(chemin);
        Graph<String, DefaultEdge> g = construireGraphe(acteurs, dico);

        String a1 = "[[Tom Hanks]]";
        String a2 = "[[Leonardo DiCaprio]]";

        System.out.println("Centre d'Hollywood : " + Exo.centreGraphe(g));
        System.out.println("petiteFamille : " + Exo.petiteFamille(g));
        System.out.println("Centralité de Tom Hanks : " + Exo.centraliteActeur(g, a1));

        System.out.println("Distance entre " + a1 + " et " + a2 + " : " + Exo.distanceActeur(g, a1, a2));
        System.out.println("Collaborateurs communs : " + Exo.collaborateursEnCommun(g, a1, a2));
        System.out.println("Collaborateurs proches (2) de " + a1 + " : " + Exo.collaborateursProches(g, a1, 2));
    }

    public static Set<String> getListeActeur(String cheminFichier) {
        Set<String> acteursUniques = new HashSet<>();
        Gson gson = new Gson();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                JsonObject objetJson = gson.fromJson(ligne, JsonObject.class);
                JsonArray cast = objetJson.getAsJsonArray("cast");
                for (JsonElement e : cast) {
                    String brut = e.getAsString();
                    int index = brut.indexOf('|');
                    if (index != -1) brut = brut.substring(index + 1);
                    acteursUniques.add(brut.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return acteursUniques;
    }

    public static Map<String, Set<String>> dicoActeur(String cheminFichier) {
        Map<String, Set<String>> map = new HashMap<>();
        Gson gson = new Gson();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                JsonObject objetJson = gson.fromJson(ligne, JsonObject.class);
                JsonArray cast = objetJson.getAsJsonArray("cast");
                List<String> acteurs = new ArrayList<>();
                for (JsonElement e : cast) {
                    String brut = e.getAsString();
                    int index = brut.indexOf('|');
                    if (index != -1) brut = brut.substring(index + 1);
                    acteurs.add(brut.trim());
                }
                for (int i = 0; i < acteurs.size(); i++) {
                    String acteur1 = acteurs.get(i);
                    for (int j = i + 1; j < acteurs.size(); j++) {
                        String acteur2 = acteurs.get(j);
                        map.computeIfAbsent(acteur1, k -> new HashSet<>()).add(acteur2);
                        map.computeIfAbsent(acteur2, k -> new HashSet<>()).add(acteur1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Graph<String, DefaultEdge> construireGraphe(Set<String> acteurs, Map<String, Set<String>> dico) {
        Graph<String, DefaultEdge> g = GraphTypeBuilder
                .undirected()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier())
                .buildGraph();

        for (String acteur : acteurs) g.addVertex(acteur);
        for (Map.Entry<String, Set<String>> entry : dico.entrySet()) {
            String a1 = entry.getKey();
            for (String a2 : entry.getValue()) {
                if (g.containsVertex(a1) && g.containsVertex(a2)) {
                    if (!a1.equals(a2)) {
                        g.addEdge(a1, a2);
                    }
                }
            }
        }
        return g;
    }
}
