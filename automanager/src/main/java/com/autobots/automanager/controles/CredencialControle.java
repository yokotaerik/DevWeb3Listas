package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.credencial.CadastroCredencialCodigoBarrasDTO;
import com.autobots.automanager.dtos.credencial.CadastroCredencialUsarioSenhaDTO;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.enumeracoes.TipoCredencial;
import com.autobots.automanager.modelos.atualizador.CredencialCodigoBarraAtualizador;
import com.autobots.automanager.modelos.atualizador.CredencialUsuarioSenhaAtualizador;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/credenciais")
public class CredencialControle {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private CredencialCodigoBarrasRepositorio credencialBarrasRepositorio;
    @Autowired
    private CredencialUsuarioSenhaRepositorio credencialUsuarioSenhaRepositorio;
    @Autowired
    private CredencialCodigoBarraAtualizador atualizadorCodigo;
    @Autowired
    private CredencialUsuarioSenhaAtualizador atualizadorNomeUsuario;

    @PostMapping("/cadastro/usuario_senha")
    public ResponseEntity<?> cadastrarCredencial(@RequestBody CadastroCredencialUsarioSenhaDTO data) {
        var usuarioDb = usuarioRepositorio.findById(data.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuarioDb.getCredenciais().removeIf(credencial -> credencial.getTipo() == TipoCredencial.USUARIO_SENHA);

        var credencial = new CredencialUsuarioSenha(data.getUsuario(), data.getSenha());
        credencial.setCriacao(new Date());

        usuarioDb.getCredenciais().add(credencial);
        this.credencialUsuarioSenhaRepositorio.save(credencial);
        this.usuarioRepositorio.save(usuarioDb);

        return ResponseEntity.ok(credencial);
    }

    @PostMapping("/cadastro/codigo_barras")
    public ResponseEntity<?> cadastrarCredencialCodigoBarras(@RequestBody CadastroCredencialCodigoBarrasDTO data) {
        var usuarioDb = usuarioRepositorio.findById(data.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuarioDb.getCredenciais().removeIf(credencial -> credencial.getTipo() == TipoCredencial.CODIGO_BARRAS);

        var credencial = new CredencialCodigoBarra(data.getCodigo());

        credencial.setCriacao(new Date());

        usuarioDb.getCredenciais().add(credencial);
        this.credencialBarrasRepositorio.save(credencial);
        this.usuarioRepositorio.save(usuarioDb);

        return ResponseEntity.ok(credencial);
    }

//	@PutMapping("/atualizar/usuario_senha")
//	public ResponseEntity<?> atualizarCredencial(@RequestBody CredencialUsuarioSenha credencial) {
//		var credencialDb = credencialUsuarioSenhaRepositorio.getById(credencial.getId());
//		if(credencial == null){
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrado");
//		}
//        atualizadorNomeUsuario.atualizar( credencialDb,credencial);
//
//		return ResponseEntity.status(HttpStatus.OK).body(credencialDb);
//	}
//
//    @PutMapping("/atualizar/codigo_barras")
//    public ResponseEntity<?> atualizarCredencialCodigo(@RequestBody CadastroCredencialCodigoBarrasDTO data) {
//        var usuarioDb = usuarioRepositorio.getById(data.getUsuarioId());
//
//        if(usuarioDb.getCredenciais().forEach(credencial ->
//        {
//            if(credencial.getTipo() == TipoCredencial.CODIGO_BARRAS){
//                usuarioDb.getCredenciais().remove(credencial);
//            }
//        }
//        ))
//
//        if(credencial == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrado");
//        }
//        atualizadorCodigo.atualizar(credencialDb,credencial);
//
//        return ResponseEntity.status(HttpStatus.OK).body(credencialDb);
//    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirCredencial(@RequestBody Credencial credencial) {
        try {
            if(credencial.getTipo() == TipoCredencial.CODIGO_BARRAS) {
                credencialBarrasRepositorio.deleteById(credencial.getId());
            } else {
                credencialUsuarioSenhaRepositorio.deleteById(credencial.getId());
            }
            return ResponseEntity.status(HttpStatus.OK).body("Credencial excluído com sucesso");
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir credencial");
        }
    }
}
