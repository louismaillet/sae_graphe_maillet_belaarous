package fr.iut45;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;

public class Test {

    public static Graph<String, DefaultEdge> grapheTest() {
        Graph<String, DefaultEdge> graph = GraphTypeBuilder
                .undirected()
                .allowingMultipleEdges(false)
                .allowingSelfLoops(false)
                .vertexSupplier(SupplierUtil.createStringSupplier())
                .edgeSupplier(SupplierUtil.createDefaultEdgeSupplier())
                .buildGraph();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("A", "E");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "E");

        return graph;
    }

    public static void main(String[] args) {
        Graph<String, DefaultEdge> graphe = grapheTest();

        System.out.println("--- Sommets ---");
        for (String v : graphe.vertexSet()) {
            System.out.println("Vertex: " + v);
        }

        System.out.println("\n--- Arêtes ---");
        for (DefaultEdge e : graphe.edgeSet()) {
            System.out.println("Edge: " + e);
        }

        System.out.println("\n--- Collaborateurs en commun (A, B) ---");
        System.out.println(Exo.collaborateursEnCommun(graphe, "A", "B"));

        System.out.println("\n--- Collaborateurs proches (A, distance 2) ---");
        System.out.println(Exo.collaborateursProches(graphe, "A", 2));

        System.out.println("\n--- Distance entre A et D ---");
        System.out.println(Exo.distanceActeur(graphe, "A", "D"));

        System.out.println("\n--- Centre du graphe ---");
        List<String> centres = Exo.centreGraphe(graphe);
        System.out.println("Centres: " + centres);

        System.out.println("\n--- Diametre du graphe ---");
        System.out.println(Exo.petiteFamille(graphe));
    }
}