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
@RequestMapping("/credencial")
public class CredencialControle {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private CredencialRepositorio credencialRepositorio;
    @Autowired
    private CredencialCodigoBarraAtualizador atualizadorCodigo;
    @Autowired
    private CredencialUsuarioSenhaAtualizador atualizadorNomeUsuario;

//
//    @GetMapping("get/unique/{id}")
//    public ResponseEntity<?> obterCredencial(@PathVariable long id)
//    {
//        var credencial = credencialRepositorio.getById(id);
//        if(credencial == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrado");
//        } else {
//            adicionadorLinkCredencial.adicionarLink(credencial);
//            return ResponseEntity.status(HttpStatus.OK).body(credencial);
//        }
//    }
//
//    @GetMapping("get/all")
//    public ResponseEntity<List<Credencial>> obterCredencials() {
//        List<Credencial> credencials = credencialRepositorio.findAll();
//        if(credencials.isEmpty()){
//            return ResponseEntity.notFound().build();
//        } else {
//            adicionadorLinkCredencial.adicionarLink(credencials);
//            return ResponseEntity.ok(credencials);
//        }
//    }

    @PostMapping("/cadastro/usuario_senha")
    public ResponseEntity<?> cadastrarCredencial(@RequestBody CadastroCredencialUsarioSenhaDTO data) {
        var cliente = usuarioRepositorio.findById(data.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        var credencial = new CredencialUsuarioSenha(data.getUsuario(), data.getSenha());


        if(cliente.getCredenciais().stream().anyMatch(c -> c.getTipo().equals(TipoCredencial.USUARIO_SENHA))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente já possui uma credencial usuário e senha");
        }
        cliente.getCredenciais().add(credencial);

        return ResponseEntity.ok("Credencial cadastrada com sucesso");
    }

    @PostMapping("/cadastro/codigo_barras")
    public ResponseEntity<?> cadastrarCredencialCodigoBarras(@RequestBody CadastroCredencialCodigoBarrasDTO data) {
        var cliente = usuarioRepositorio.findById(data.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        var credencial = new CredencialCodigoBarra(data.getCodigo());

        if(cliente.getCredenciais().stream().anyMatch(c -> c.getTipo().equals(TipoCredencial.CODIGO_BARRAS))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente já possui uma credencial código de barras");
        }
        cliente.getCredenciais().add(credencial);

        return ResponseEntity.ok("Credencial cadastrada com sucesso");
    }

	@PutMapping("/atualizar/usuario_senha")
	public ResponseEntity<?> atualizarCredencial(@RequestBody CredencialUsuarioSenha credencial) {
		Credencial credencialDb = credencialRepositorio.getById(credencial.getId());
		if(credencial == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrado");
		}
        atualizadorNomeUsuario.atualizar((CredencialUsuarioSenha) credencialDb,credencial);

		return ResponseEntity.status(HttpStatus.OK).body(credencialDb);
	}

    @PutMapping("/atualizar/codigo_barras")
    public ResponseEntity<?> atualizarCredencialCodigo(@RequestBody CredencialCodigoBarra credencial) {
        Credencial credencialDb = credencialRepositorio.getById(credencial.getId());
        if(credencial == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrado");
        }
        atualizadorCodigo.atualizar((CredencialCodigoBarra) credencialDb,credencial);

        return ResponseEntity.status(HttpStatus.OK).body(credencialDb);
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<?> excluirCredencial(@RequestBody Long id) {
        try {
            credencialRepositorio.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Credencial excluído com sucesso");
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir credencial");
        }
    }
}
