package fr.iut45;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Exo {

    private Exo() {}

    // 3.2 Collaborateurs en commun
    public static Set<String> collaborateursEnCommun(Graph<String, DefaultEdge> g, String v1, String v2) {
        Set<String> voisinv1 = new HashSet<>();
        Set<String> voisinv2 = new HashSet<>();

        for (String voisin : Graphs.neighborListOf(g, v1)) {
            voisinv1.add(voisin);
        }

        for (String voisin : Graphs.neighborListOf(g, v2)) {
            voisinv2.add(voisin);
        }

        Set<String> communs = new HashSet<>();
        for (String voisin : voisinv1) {
            if (voisinv2.contains(voisin)) {
                communs.add(voisin);
            }
        }

        return communs;
    }

    // 3.3 Collaborateurs proches jusqu'à une distance k
    public static Set<String> collaborateursProches(Graph<String, DefaultEdge> g, String v1, int k) {
        if (!g.containsVertex(v1)) return Collections.emptySet();

        Set<String> collaborateurs = new HashSet<>();
        collaborateurs.add(v1);

        for (int i = 1; i <= k; i++) {
            Set<String> collaborateursDirects = new HashSet<>();
            for (String c : collaborateurs) {
                for (String voisin : Graphs.neighborListOf(g, c)) {
                    if (!collaborateurs.contains(voisin)) {
                        collaborateursDirects.add(voisin);
                    }
                }
            }
            collaborateurs.addAll(collaborateursDirects);
        }

        return collaborateurs;
    }
    
    // Distance minimale entre deux acteurs dans le graphe
    public static int distanceActeur(Graph<String, DefaultEdge> g, String v1, String v2) {
        if (!g.containsVertex(v1) || !g.containsVertex(v2)) {
            return -1;
        }

        if (v1.equals(v2)) {
            return 0;
        }

        Set<String> visite = new HashSet<>();
        Queue<String> file = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        file.add(v1);
        visite.add(v1);
        distance.put(v1, 0);

        while (!file.isEmpty()) {
            String courant = file.poll();
            int dist = distance.get(courant);

            for (String voisin : Graphs.neighborListOf(g, courant)) {
                if (!visite.contains(voisin)) {
                    file.add(voisin);
                    visite.add(voisin);
                    distance.put(voisin, dist + 1);

                    if (voisin.equals(v2)) {
                        return dist + 1;
                    }
                }
            }
        }

        return -1;
    }

    // Calcule la centralité d'un acteur : la plus grande distance entre cet acteur et tous les autres
    public static int centraliteActeur(Graph<String, DefaultEdge> g, String acteur) {
        if (!g.containsVertex(acteur)) {
            return -1;
        }

        Set<String> visites = new HashSet<>();
        Queue<String> file = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        file.add(acteur);
        visites.add(acteur);
        distance.put(acteur, 0);

        int max = 0;

        while (!file.isEmpty()) {
            String courant = file.poll();
            int dist = distance.get(courant);

            for (String voisin : Graphs.neighborListOf(g, courant)) {
                if (!visites.contains(voisin)) {
                    file.add(voisin);
                    visites.add(voisin);
                    distance.put(voisin, dist + 1);

                    if (dist + 1 > max) {
                        max = dist + 1;
                    }
                }
            }
        }

        if (distance.size() < g.vertexSet().size()) {
            return -1;
        }

        return max;
    }

    // Trouve le ou les centres du graphe
    public static List<String> centreGraphe(Graph<String, DefaultEdge> g) {
        List<String> centres = new ArrayList<>();
        int minCentralite = Integer.MAX_VALUE;

        for (String v : g.vertexSet()) {
            int c = centraliteActeur(g, v);
            if (c >= 0) {
                if (c < minCentralite) {
                    minCentralite = c;
                    centres.clear();
                    centres.add(v);
                } else if (c == minCentralite) {
                    centres.add(v);
                }
            }
        }

        return centres;
    }

    // Calcule la centralité maximale parmi tous les acteurs
    public static int petiteFamille(Graph<String, DefaultEdge> g) {
        int max = 0;

        for (String v : g.vertexSet()) {
            int c = centraliteActeur(g, v);

            if (c == -1) {
                return -1;
            }

            if (c > max) {
                max = c;
            }
        }

        return max;
    }
}
