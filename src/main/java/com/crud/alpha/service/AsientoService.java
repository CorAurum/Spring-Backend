package com.crud.alpha.service;

import com.crud.alpha.clase.Omnibus.Asiento;
import com.crud.alpha.clase.Omnibus.Omnibus;
import com.crud.alpha.clase.exceptions.EntityNotFoundException;
import com.crud.alpha.repository.AsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    @Autowired
    private OmnibusService omnibusService;

    // Listar asientos por nroCoche
    public List<Asiento> listarPorNroCoche(int nroCoche) {
        return asientoRepository.findByOmnibus_nroCoche(nroCoche);
    }

    // Crear varios asientos para un omnibus
    public void crearAsientosParaOmnibus(int nroCoche, int cantidadAsientos) throws EntityNotFoundException {
        // Buscar omnibus por nroCoche
        Omnibus omnibus = omnibusService.findEntity(nroCoche);
        // Crear asientos y asociarlos al omnibus
        for (int i = 1; i <= cantidadAsientos; i++) {
            Asiento asiento = new Asiento();
            asiento.setNumero(i);
            asiento.setOmnibus(omnibus);
            asientoRepository.save(asiento);
        }
    }
}
