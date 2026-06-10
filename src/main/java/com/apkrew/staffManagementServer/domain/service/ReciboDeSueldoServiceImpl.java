package com.apkrew.staffManagementServer.domain.service;

import com.apkrew.staffManagementServer.domain.dto.DetalleRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.DetalleResponseDTO;
import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoRequestDTO;
import com.apkrew.staffManagementServer.domain.dto.ReciboDeSueldoResponseDTO;
import com.apkrew.staffManagementServer.domain.entity.DetalleReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.entity.Empleado;
import com.apkrew.staffManagementServer.domain.entity.ItemReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.entity.ReciboDeSueldo;
import com.apkrew.staffManagementServer.domain.entity.Usuario;
import com.apkrew.staffManagementServer.domain.repository.BaseRepository;
import com.apkrew.staffManagementServer.domain.repository.ReciboDeSueldoRepository;
import com.apkrew.staffManagementServer.exceptions.ErrorServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.apkrew.staffManagementServer.seguridad.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
public class ReciboDeSueldoServiceImpl
        extends BaseServiceImpl<ReciboDeSueldo, String>
        implements ReciboDeSueldoService {

    private final ReciboDeSueldoRepository reciboDeSueldoRepository;
    private final EmpleadoService empleadoService;
    private final ItemReciboDeSueldoService itemReciboDeSueldoService;
    private final UsuarioService usuarioService;

    public ReciboDeSueldoServiceImpl(
            BaseRepository<ReciboDeSueldo, String> baseRepository,
            ReciboDeSueldoRepository reciboDeSueldoRepository,
            EmpleadoService empleadoService,
            ItemReciboDeSueldoService itemReciboDeSueldoService,
            UsuarioService usuarioService) {

        super(baseRepository);
        this.reciboDeSueldoRepository = reciboDeSueldoRepository;
        this.empleadoService = empleadoService;
        this.itemReciboDeSueldoService = itemReciboDeSueldoService;
        this.usuarioService = usuarioService;
    }

    @Override
    public boolean validar(ReciboDeSueldo entity, String caso)
            throws ErrorServiceException {

        try {

            if (entity.getEmpleado() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el empleado");
            }

            if (entity.getFechaDePago() == null) {
                throw new ErrorServiceException(
                        "Debe indicar la fecha de pago");
            }

            if (entity.getTotalPago() == null || entity.getTotalPago() <= 0) {
                throw new ErrorServiceException(
                        "El total de pago debe ser mayor a 0");
            }

            if (entity.getDetalles() == null ||
                    entity.getDetalles().isEmpty()) {
                throw new ErrorServiceException(
                        "Debe indicar al menos un detalle");
            }

            return true;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public double calcularTotal(String id) throws Exception {
        try {
            ReciboDeSueldo recibo = findById(id);

            double total = recibo.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getValor())
                    .sum();

            recibo.setTotalPago(total);
            reciboDeSueldoRepository.save(recibo);

            return total;

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) throws Exception {
        try {
            if (reciboDeSueldoRepository.existsByIdAndEliminadoFalse(id)) {
                ReciboDeSueldo recibo = reciboDeSueldoRepository.findByIdAndEliminadoFalse(id)
                        .orElseThrow(() -> new ErrorServiceException("El recibo no existe o ya fue eliminado"));
                recibo.setEliminado(true);
                if (recibo.getDetalles() != null) {
                    for (DetalleReciboDeSueldo detalle : recibo.getDetalles()) {
                        detalle.setEliminado(true);
                    }
                }
                reciboDeSueldoRepository.save(recibo);
                return true;
            } else {
                throw new ErrorServiceException("El objeto ya fue eliminado o no existe");
            }
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception("Hubo un problema eliminando el objeto");
        }
    }

    @Override
    @Transactional
    public ReciboDeSueldoResponseDTO saveFromDTO(ReciboDeSueldoRequestDTO dto)
            throws Exception {

        try {
            validarDTO(dto);

            Empleado empleado;
            try {
                empleado = empleadoService.findById(dto.getEmpleadoId());
            } catch (Exception ex) {
                throw new ErrorServiceException("El empleado indicado no existe");
            }

            ReciboDeSueldo recibo = ReciboDeSueldo.builder()
                    .empleado(empleado)
                    .fechaDePago(dto.getFechaDePago())
                    .mesPago(dto.getMesPago())
                    .observacion(dto.getObservacion())
                    .detalles(new ArrayList<>())
                    .build();

            double total = 0;

            for (DetalleRequestDTO detDTO : dto.getDetalles()) {
                ItemReciboDeSueldo item;
                try {
                    item = itemReciboDeSueldoService.findById(detDTO.getItemReciboDeSueldoId());
                } catch (Exception ex) {
                    throw new ErrorServiceException(
                            "El item de recibo de sueldo con id "
                                    + detDTO.getItemReciboDeSueldoId()
                                    + " no existe");
                }

                DetalleReciboDeSueldo detalle = DetalleReciboDeSueldo.builder()
                        .cantidad(detDTO.getCantidad())
                        .valor(detDTO.getValor())
                        .tipoDetalleRecibo(detDTO.getTipoDetalleRecibo())
                        .itemReciboDeSueldo(item)
                        .reciboDeSueldo(recibo)
                        .build();

                recibo.getDetalles().add(detalle);
                total += detDTO.getCantidad() * detDTO.getValor();
            }

            recibo.setTotalPago(total);
            recibo = reciboDeSueldoRepository.save(recibo);

            return toResponseDTO(recibo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public ReciboDeSueldoResponseDTO updateFromDTO(String id, ReciboDeSueldoRequestDTO dto)
            throws Exception {

        try {
            validarDTO(dto);

            ReciboDeSueldo recibo = reciboDeSueldoRepository
                    .findByIdAndEliminadoFalse(id)
                    .orElseThrow(() -> new ErrorServiceException(
                            "El recibo de sueldo con id " + id + " no existe"));

            Empleado empleado;
            try {
                empleado = empleadoService.findById(dto.getEmpleadoId());
            } catch (Exception ex) {
                throw new ErrorServiceException("El empleado indicado no existe");
            }

            recibo.setEmpleado(empleado);
            recibo.setFechaDePago(dto.getFechaDePago());
            recibo.setMesPago(dto.getMesPago());
            recibo.setObservacion(dto.getObservacion());

            // Clear details safely to preserve Hibernate collection instance and trigger orphan removal
            recibo.getDetalles().clear();

            double total = 0;

            for (DetalleRequestDTO detDTO : dto.getDetalles()) {
                ItemReciboDeSueldo item;
                try {
                    item = itemReciboDeSueldoService.findById(detDTO.getItemReciboDeSueldoId());
                } catch (Exception ex) {
                    throw new ErrorServiceException(
                            "El item de recibo de sueldo con id "
                                    + detDTO.getItemReciboDeSueldoId()
                                    + " no existe");
                }

                DetalleReciboDeSueldo detalle = DetalleReciboDeSueldo.builder()
                        .cantidad(detDTO.getCantidad())
                        .valor(detDTO.getValor())
                        .tipoDetalleRecibo(detDTO.getTipoDetalleRecibo())
                        .itemReciboDeSueldo(item)
                        .reciboDeSueldo(recibo)
                        .build();

                recibo.getDetalles().add(detalle);
                total += detDTO.getCantidad() * detDTO.getValor();
            }

            recibo.setTotalPago(total);
            recibo = reciboDeSueldoRepository.save(recibo);

            return toResponseDTO(recibo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public ReciboDeSueldoResponseDTO findResponseById(String id)
            throws Exception {

        try {
            ReciboDeSueldo recibo = findById(id);
            return toResponseDTO(recibo);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public List<ReciboDeSueldoResponseDTO> findAllResponse()
            throws Exception {

        try {
            List<ReciboDeSueldo> recibos = findAll();
            List<ReciboDeSueldoResponseDTO> dtos = new ArrayList<>();

            for (ReciboDeSueldo recibo : recibos) {
                dtos.add(toResponseDTO(recibo));
            }

            return dtos;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public org.springframework.data.domain.Page<ReciboDeSueldoResponseDTO> findAllResponse(org.springframework.data.domain.Pageable pageable)
            throws Exception {

        try {
            org.springframework.data.domain.Page<ReciboDeSueldo> recibos = findAll(pageable);
            return recibos.map(this::toResponseDTO);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public List<ReciboDeSueldoResponseDTO> findAllByEmpleadoId(String empleadoId)
            throws Exception {

        try {
            List<ReciboDeSueldo> recibos = reciboDeSueldoRepository
                    .findByEmpleadoIdAndEliminadoFalse(empleadoId);
            List<ReciboDeSueldoResponseDTO> dtos = new ArrayList<>();
            for (ReciboDeSueldo recibo : recibos) {
                dtos.add(toResponseDTO(recibo));
            }
            return dtos;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    @Transactional
    public Page<ReciboDeSueldoResponseDTO> findAllByEmpleadoId(String empleadoId, Pageable pageable)
            throws Exception {

        try {
            Page<ReciboDeSueldo> recibos = reciboDeSueldoRepository
                    .findByEmpleadoIdAndEliminadoFalse(empleadoId, pageable);
            return recibos.map(this::toResponseDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    @Override
    public Page<ReciboDeSueldoResponseDTO> findAllForCurrentUser(Pageable pageable)
            throws Exception {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            Usuario usuario = usuarioService.findById(principal.getUserId());
            return findAllByEmpleadoId(usuario.getPersona().getId(), pageable);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    private void validarDTO(ReciboDeSueldoRequestDTO dto)
            throws ErrorServiceException {

        if (dto.getEmpleadoId() == null || dto.getEmpleadoId().isBlank()) {
            throw new ErrorServiceException(
                    "Debe indicar el empleado");
        }

        if (dto.getFechaDePago() == null) {
            throw new ErrorServiceException(
                    "Debe indicar la fecha de pago");
        }

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ErrorServiceException(
                    "Debe indicar al menos un detalle");
        }

        if (reciboDeSueldoRepository.findByEmpleadoIdAndMesPagoAndEliminadoFalse(dto.getEmpleadoId(), dto.getMesPago()) != null) {
            throw new ErrorServiceException(
                    "Ya existe un recibo de sueldo del empleado para el mes seleccionado.");
        }

        for (int i = 0; i < dto.getDetalles().size(); i++) {
            DetalleRequestDTO det = dto.getDetalles().get(i);

            if (det.getCantidad() == null || det.getCantidad() <= 0) {
                throw new ErrorServiceException(
                        "La cantidad del detalle " + (i + 1)
                                + " debe ser mayor a 0");
            }

            if (det.getValor() == null || det.getValor() <= 0) {
                throw new ErrorServiceException(
                        "El valor del detalle " + (i + 1)
                                + " debe ser mayor a 0");
            }

            if (det.getTipoDetalleRecibo() == null) {
                throw new ErrorServiceException(
                        "Debe indicar el tipo de detalle del detalle "
                                + (i + 1));
            }

            if (det.getItemReciboDeSueldoId() == null
                    || det.getItemReciboDeSueldoId().isBlank()) {
                throw new ErrorServiceException(
                        "Debe indicar el item de recibo de sueldo del detalle "
                                + (i + 1));
            }
        }
    }

    private ReciboDeSueldoResponseDTO toResponseDTO(ReciboDeSueldo recibo) {
        ReciboDeSueldoResponseDTO dto = ReciboDeSueldoResponseDTO.builder()
                .id(recibo.getId())
                .empleadoId(recibo.getEmpleado().getId())
                .empleadoNombre(recibo.getEmpleado().getNombre())
                .empleadoApellido(recibo.getEmpleado().getApellido())
                .fechaDePago(recibo.getFechaDePago())
                .mesPago(recibo.getMesPago())
                .totalPago(recibo.getTotalPago())
                .observacion(recibo.getObservacion())
                .build();

        if (recibo.getDetalles() != null) {
            dto.setDetalles(new ArrayList<>());
            for (DetalleReciboDeSueldo det : recibo.getDetalles()) {
                DetalleResponseDTO detDTO = DetalleResponseDTO.builder()
                        .id(det.getId())
                        .cantidad(det.getCantidad())
                        .valor(det.getValor())
                        .tipoDetalleRecibo(det.getTipoDetalleRecibo())
                        .itemReciboDeSueldoId(det.getItemReciboDeSueldo().getId())
                        .itemReciboDeSueldoNombre(
                                det.getItemReciboDeSueldo().getNombre())
                        .build();
                dto.getDetalles().add(detDTO);
            }
        }

        return dto;
    }

    public byte[] generarPdf(String id) {

        ReciboDeSueldo recibo = reciboDeSueldoRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Recibo de sueldo no encontrado"));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph titulo = new Paragraph("RECIBO DE SUELDO")
                    .setFontSize(18);

            document.add(titulo);

            document.add(new Paragraph(" "));

            // Datos empleado
            document.add(new Paragraph(
                    "Empleado: "
                            + recibo.getEmpleado().getApellido()
                            + ", "
                            + recibo.getEmpleado().getNombre()));

            document.add(new Paragraph(
                    "DNI: "
                            + recibo.getEmpleado().getDni()));

            document.add(new Paragraph(
                    "Cargo: "
                            + recibo.getEmpleado().getTipoEmpleado()));

            document.add(new Paragraph(
                    "Fecha de pago: "
                            + new SimpleDateFormat("dd/MM/yyyy")
                            .format(recibo.getFechaDePago())));

            String nombreMes = Month.of(recibo.getMesPago())
                    .getDisplayName(TextStyle.FULL, new Locale("es", "AR"));

            nombreMes = Character.toUpperCase(nombreMes.charAt(0))
                    + nombreMes.substring(1);

            document.add(new Paragraph(
                    "Mes liquidado: " + nombreMes));

            document.add(new Paragraph(" "));

            // Tabla de conceptos
            Table table = new Table(UnitValue.createPercentArray(
                    new float[]{4, 2, 2, 2}))
                    .useAllAvailableWidth();

            table.addHeaderCell("Concepto");
            table.addHeaderCell("Cantidad");
            table.addHeaderCell("Valor");
            table.addHeaderCell("Total");

            for (DetalleReciboDeSueldo detalle : recibo.getDetalles()) {

                double subtotal =
                        detalle.getCantidad() * detalle.getValor();

                table.addCell(
                        detalle.getItemReciboDeSueldo().getNombre());

                table.addCell(
                        String.valueOf(detalle.getCantidad()));

                table.addCell(
                        String.format("$ %.2f",
                                detalle.getValor()));

                table.addCell(
                        String.format("$ %.2f",
                                subtotal));
            }

            document.add(table);

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Total a cobrar: $ "
                                    + String.format("%.2f",
                                    recibo.getTotalPago())));

            if (recibo.getObservacion() != null &&
                    !recibo.getObservacion().isBlank()) {

                document.add(new Paragraph(" "));
                document.add(
                        new Paragraph("Observaciones:"));

                document.add(
                        new Paragraph(recibo.getObservacion()));
            }

            document.add(new Paragraph("\n\n"));

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al generar PDF", e);
        }
    }
}
