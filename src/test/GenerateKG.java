package test;

import org.apache.jena.irix.IRIs;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class GenerateKG {

    /*
     * A "Model" in Jena is what represents a graph, but is mutable: new nodes and arcs can be added; nodes or arcs can be removed; etc.
     */
    protected Model kg;
    /*
     * This is to define a "base URI" for the identifiers of nodes and relations.
     */
    private String base;

    /*
     * Initialises an empty Jena Model (graph with no node nor arcs) and a few prefixes.
     */
    public GenerateKG() {
        this.kg = ModelFactory.createDefaultModel();
        kg.setNsPrefix("ex", "http://example.org/");
        kg.setNsPrefix("owl", OWL.NS);
        kg.setNsPrefix("rdfs", RDFS.uri);
        kg.setNsPrefix("xsd", XSD.NS);
    }

    /*
     * Sets the base used to generate triples from Turtle bits that have relative IRIs.
     * The base must be a valid IRI.
     */
    public boolean setBase(String base) {
        boolean check = IRIs.check(base);
        if(check) {
            this.base = base;
        }
        return check;
    }

    /*
     * Sets a new namespace prefix.
     * The namespace must be a valid IRI.
     */
    public boolean setNsPrefix(String prefix, String ns) {
        boolean check = IRIs.check(ns);
        if(check) {
            kg.setNsPrefix(prefix, ns);
        }
        return check;
    }

    /*
     * Serialises the prefix declarations in Turtle for all prefixes defined in the Jena Model
     */
    String prefixesInTurtle() {
        String ttlPrefixes = "";
        for(Map.Entry<String, String> entry : kg.getNsPrefixMap().entrySet()) {
            ttlPrefixes += "@prefix " + entry.getKey() + ": <" + entry.getValue() + ">.\n";
        }
        return ttlPrefixes;
    }

    /*
     * Add triples written in a String as Turtle code. Does not require declaring the prefixes if they are already in the NsPrefixes of the model
     */
    public void addTriples(String ttl) {
        String prefixedTurtle = this.prefixesInTurtle() + "\n" + ttl;
        InputStream is = new ByteArrayInputStream(prefixedTurtle.getBytes());
        kg.read(is,base,"Turtle");
    }

    /*
     * Serialises the knowledge graph to Turtle and display it on the console.
     */
    public void serialise() {
        kg.write(System.out,"Turtle");
    }

    /*
     * Serialises the knowledge graph to Turtle into an output stream given as parameter (e.g., a FileOutputStream)
     */
    public void serialise(OutputStream os) {
        kg.write(os,"Turtle");
    }

    /*
     * Main execution method
     */
    public static void main(String[] args) {
        // Create a new KG generator
        GenerateKG gen = new GenerateKG();
        // Set the base URI to a custom value
        gen.setBase("http://mydomain.org/weatherkg/");
        // Define an additional prefix for the ontology namespace
        gen.setNsPrefix("onto", "http://mydomain.org/ontology/");
        // Add a single triple
        gen.addTriples("<> a onto:Entity .");
        // Add a bunch of triples at once
        gen.addTriples("<> rdfs:label \"Test entity\"@en;"
                + "  rdfs:commnt \"This is a test\"@en .");
        gen.serialise();

    }
}