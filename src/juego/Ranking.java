package juego;

import java.util.*;

public class Ranking {
    private Map<String, Long> ranking;

    public Ranking() {
        this.ranking = new HashMap<>();
    }

    public void actualizarRanking(String nombreJugador, long tiempo) {
        if (!ranking.containsKey(nombreJugador) || ranking.get(nombreJugador) > tiempo) {
            ranking.put(nombreJugador, tiempo);
        }
    }
    public void actualizarRankingDesdeArchivo(List<String> rankingList) {
        for (String entry : rankingList) {
            String[] parts = entry.split(" segundos ");
            if (parts.length == 2) {
                String nombreJugador = parts[1];
                long tiempo = Long.parseLong(parts[0]);
                actualizarRanking(nombreJugador, tiempo);
            }
        }
    }
    public List<String> obtenerRanking() {
        List<Map.Entry<String, Long>> listaRanking = new ArrayList<>(ranking.entrySet());
        listaRanking.sort(Map.Entry.comparingByValue());
        List<String> rankingStrList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : listaRanking) {
            String rankingStr = entry.getValue() + " segundos " + entry.getKey();
            rankingStrList.add(rankingStr);
        }
        return rankingStrList;
    }

    public void eliminarEntrada(String nombreJugador, long tiempo) {
        Iterator<Map.Entry<String, Long>> iterator = ranking.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getKey().equals(nombreJugador) && entry.getValue().equals(tiempo)) {
                iterator.remove(); // Elimina la entrada del iterador
                return;
            }
        }
    }



}