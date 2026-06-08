package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.ArticuloRequestDTO;
import com.apkrew.staffManagementServer.domain.entity.Articulo;
import com.apkrew.staffManagementServer.domain.entity.Imagen;
import com.apkrew.staffManagementServer.domain.entity.UnidadDeMedida;
import com.apkrew.staffManagementServer.domain.enums.TipoImagen;
import com.apkrew.staffManagementServer.domain.repository.ArticuloRepository;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ArticuloServiceImpl extends BaseServiceImpl<Articulo,String> implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final UnidadDeMedidaServiceImpl unidadDeMedidaService;
    private final ImageServiceImpl imageService;

    public ArticuloServiceImpl(BaseRepository<Articulo, String> baseRepository,
                               ArticuloRepository articuloRepository,
                               UnidadDeMedidaServiceImpl unidadDeMedidaService,
                               ImageServiceImpl imageService) {
        super(baseRepository);
        this.articuloRepository = articuloRepository;
        this.unidadDeMedidaService = unidadDeMedidaService;
        this.imageService = imageService;
    }

    @Transactional
    public Articulo crearArticulo(ArticuloRequestDTO dto, MultipartFile imagen) throws ErrorServiceException {
        try {
            UnidadDeMedida unidadDeMedida = unidadDeMedidaService.findById(dto.getUnidadDeMedida());

            Articulo articulo = new Articulo();
            articulo.setNombre(dto.getNombre());
            articulo.setDescripcion(dto.getDescripcion());
            articulo.setSinTAC(dto.isSinTAC());
            articulo.setEsIngrediente(dto.isEsIngrediente());
            articulo.setUnidadDeMedida(unidadDeMedida);

            if (imagen != null && !imagen.isEmpty()) {
                Imagen img = new Imagen();
                img.setNombre(imagen.getOriginalFilename());
                img.setMime(imagen.getContentType());
                img.setContenido(imagen.getBytes());
                img.setTipoImagen(TipoImagen.PRODUCTO);
                img = imageService.save(img);
                articulo.setImagen(img);
            }

            validar(articulo, "SAVE");
            return save(articulo);
        } catch (IOException e) {
            throw new ErrorServiceException("Error procesando la imagen.");
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Transactional
    public Articulo editarArticulo(String id, ArticuloRequestDTO dto, MultipartFile imagen) throws ErrorServiceException {
        try {
            Articulo articulo = findById(id);
            UnidadDeMedida unidadDeMedida = unidadDeMedidaService.findById(dto.getUnidadDeMedida());

            articulo.setNombre(dto.getNombre());
            articulo.setDescripcion(dto.getDescripcion());
            articulo.setSinTAC(dto.isSinTAC());
            articulo.setEsIngrediente(dto.isEsIngrediente());
            articulo.setUnidadDeMedida(unidadDeMedida);

            if (imagen != null && !imagen.isEmpty()) {
                Imagen img = new Imagen();
                img.setNombre(imagen.getOriginalFilename());
                img.setMime(imagen.getContentType());
                img.setContenido(imagen.getBytes());
                img.setTipoImagen(TipoImagen.PRODUCTO);
                img = imageService.save(img);
                articulo.setImagen(img);
            }

            validar(articulo, "UPDATE");
            return articuloRepository.save(articulo);
        } catch (IOException e) {
            throw new ErrorServiceException("Error procesando la imagen.");
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    public boolean validar(Articulo entity, String caso) throws ErrorServiceException {
        try {
            if (entity.getNombre() == null || entity.getNombre().isBlank()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (caso.equals("SAVE")) {
                if (articuloRepository.existsByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre())) {
                    throw new ErrorServiceException("El artículo ya existe en el sistema");
                }
            } else {
                Articulo articulo = articuloRepository.findByNombreIgnoreCaseAndEliminadoFalse(entity.getNombre());
                if (articulo != null) {
                    if(!articulo.getId().equals(entity.getId())) {
                        throw new ErrorServiceException("El artículo no existe en el sistema");
                    }
                }
            }

            return true;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}
