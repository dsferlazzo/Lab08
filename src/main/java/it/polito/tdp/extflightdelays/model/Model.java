package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
	
	public String creaGrafo(int distanza) {
		grafo = new SimpleWeightedGraph<Airport,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Airport> listaAereoporti = this.dao.getAirportsConnected(distanza);
		Map<Integer, Airport> mappaIdAereoporti = new HashMap<Integer, Airport>();
		
		//AGGIUNGO I VERTICI
		for(Airport a:listaAereoporti) {
			mappaIdAereoporti.put(a.getId(), a);
		}
		Graphs.addAllVertices(grafo, listaAereoporti);
		
		
		//AGGIUNGO GLI ARCHI
		Airport partenza;
		Airport arrivo;
		List<CoppiaId> coppieId = dao.getCoppieId(distanza);
		String s1="";
		for(CoppiaId cid : coppieId) {
			partenza = mappaIdAereoporti.get(cid.getIdPartenza());
			arrivo = mappaIdAereoporti.get(cid.getIdArrivo());
			if(partenza!=null && arrivo!=null)		//POICHE NON E DETTO CHE ENTRAMBI GLI ID TROVATI DALLA QUERY FACCIANO PARTE DEL GRAFO
			{
				DefaultWeightedEdge e1 = this.grafo.addEdge(partenza, arrivo);
				if(e1!=null)	//PER EVITARE EVENTUALI DUPLICATI
				{
					this.grafo.setEdgeWeight(e1, cid.getAVGDistanza());
					s1+= "Partenza: " + partenza + "  Arrivo: " + arrivo + "  Distanza media: " + cid.getAVGDistanza() + "\n";
					//AGGIUNGO, MENTRE LI AGGIUNGO NEL GRAFO, GLI ARCHI ANCHE ALLA STRINGA DI OUTPUT
				}
			}
			
		}
		//System.out.println(grafo.vertexSet().size());
		//System.out.println(grafo.edgeSet().size());
		
		String result = "Vertici del grafo: "+grafo.vertexSet().size()+"\n"+
				"Archi del grafo: " +grafo.edgeSet().size()+"\n" + s1;
		
	
		return result;
		
	}

}
