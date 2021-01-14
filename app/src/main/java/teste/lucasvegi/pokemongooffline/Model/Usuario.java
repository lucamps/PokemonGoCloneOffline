package teste.lucasvegi.pokemongooffline.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.Util.MyApp;
import teste.lucasvegi.pokemongooffline.Util.TimeUtil;

/**
 * Created by Lucas on 08/12/2016.
 */
public class Usuario {
    private String login;
    private String senha;
    private String nome;
    private String sexo;
    private String foto;
    private String dtCadastro;
    private Map<Pokemon,List<PokemonCapturado>> pokemons;
    private int nivel;
    private int xp;

    public Usuario(){

    }

    protected Usuario(String lg) {
        this.login = lg;
        pokemons = new HashMap<Pokemon, List<PokemonCapturado>>();

        preencherCapturas();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Map<Pokemon, List<PokemonCapturado>> getPokemons() {
        return pokemons;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    private void preencherCapturas(){
        //TODO: verificar se é necessário sincronizar com o server antes dessa operação. Nova operação da controladora será necessária para isso!

        try {
            Log.i("DAO_USER", "Preenchendo capturas...");

            //Select p.idPokemon idPokemon, pu.latitude latitude, pu.longitude longitude, pu.dtCaptura dtCaptura from pokemon p, usuario u, pokemonusuario pu where p.idPokemon = pu.idPokemon and u.login = pu.login and u.login = login
            Cursor cPkmn = BancoDadosSingleton.getInstance().buscar("pokemon p, usuario u, pokemonusuario pu",
                    new String[]{"p.idPokemon idPokemon", "pu.latitude latitude", "pu.longitude longitude", "pu.dtCaptura dtCaptura", "pu.estaBloqueado estaBloqueado"},
                    "p.idPokemon = pu.idPokemon and u.login = pu.login and u.login = '" + this.login + "'",
                    "p.idPokemon asc");

            //obtem lista de pokemons da controladora geral
            List<Pokemon> listPkmn = ControladoraFachadaSingleton.getInstance().getPokemons();

            while (cPkmn.moveToNext()) {

                int idPkmn = cPkmn.getColumnIndex("idPokemon");
                int lat = cPkmn.getColumnIndex("latitude");
                int longi = cPkmn.getColumnIndex("longitude");
                int dtCaptura = cPkmn.getColumnIndex("dtCaptura");
                int estaBloqueado = cPkmn.getColumnIndex("estaBloqueado");

                //procura o pokemon retornado do banco na lista de pokemons da controladora geral
                for (Pokemon pokemon : listPkmn) {
                    if (pokemon.getNumero() == cPkmn.getInt(idPkmn)) {

                        //cria objeto PokemonCapturado com informações vindas do banco
                        PokemonCapturado pc = new PokemonCapturado();
                        pc.setLatitude(cPkmn.getDouble(lat));
                        pc.setLongitude(cPkmn.getDouble(longi));
                        pc.setDtCaptura(cPkmn.getString(dtCaptura));
                        pc.setEstaBloqueado(cPkmn.getInt(estaBloqueado));

                        //verifica se lista de algum pokemon ainda não existe
                        if(pokemons.get(pokemon) == null) {
                            pokemons.put(pokemon, new ArrayList<PokemonCapturado>());
                            Log.i("DAO_USER", "Preenchendo captura nova");
                        }else{
                            Log.i("DAO_USER", "Preenchendo captura conhecida");
                        }

                        //adiciona o pokemon na lista da sua categoria
                        pokemons.get(pokemon).add(pc);
                    }
                }
            }
            cPkmn.close();
        }catch (Exception e){
            Log.e("DAO_USER", "ERRO: " + e.getMessage());
        }

    }

    public void somarDoces(Pokemon p, int num){
        //Obtendo a linha da tabela de Doces referentes ao pokemon p
        Cursor cDoce = BancoDadosSingleton.getInstance().buscar("pokemon p, doce d",
                new String[]{"d.idDoce idDoce","d.nome nome","d.quant quant"},
                "p.idDoce = d.idDoce and d.idDoce = '" + p.getIdDoce() + "'",null);
        cDoce.moveToNext(); //obs: fora do while pois deve haver apenas uma linha de resposta

        //TODO: seria bom usar um try-catch?

        //Salvando os índices da tabela
        int idDoce = cDoce.getColumnIndex("idDoce");
        int nome = cDoce.getColumnIndex("nome");
        int quantDoces = cDoce.getColumnIndex("quant");

        //Prepara valores para serem persistidos no banco
        ContentValues valoresDoce = new ContentValues();
        valoresDoce.put("idDoce",cDoce.getInt(idDoce));
        valoresDoce.put("nome",cDoce.getString(nome));
        valoresDoce.put("quant",cDoce.getInt(quantDoces)+num); //soma 3 na qtd de doces

        //Atualizando o banco
        BancoDadosSingleton.getInstance().atualizar("doce",valoresDoce,"idDoce = " + "'" +p.getIdDoce() + "'");

        Log.i("DOCES", "Quantidade de doces do " + cDoce.getString(nome)+ " = " + (int)(cDoce.getInt(quantDoces)+num) );

    }

    public boolean capturar(Aparecimento aparecimento){
        try {
            Log.i("CAPTURA", "Capturando " + aparecimento.getPokemon().getNome());

            //TODO: RESOLVIDO - procura na lista de pokemons da controladora o pokemon capturado.
            //Pokemon pkmnAux = ControladoraFachadaSingleton.getInstance().convertPokemonSerializableToObject(aparecimento.getPokemon());
            Pokemon pkmnAux = aparecimento.getPokemon();

            //Obtem timeStamp da captura
            Map<String, String> ts = TimeUtil.getHoraMinutoSegundoDiaMesAno();
            String dtCap = ts.get("dia") + "/" + ts.get("mes") + "/" + ts.get("ano") + " " + ts.get("hora") + ":" + ts.get("minuto") + ":" + ts.get("segundo");

            //Prepara valores para serem persistidos no banco
            ContentValues valores = new ContentValues();
            valores.put("login", this.login);
            valores.put("idPokemon", pkmnAux.getNumero());
            valores.put("dtCaptura", dtCap);
            valores.put("latitude", aparecimento.getLatitude());
            valores.put("longitude", aparecimento.getLongitude());
            valores.put("estaBloqueado",0);

            //Persiste captura no banco
            BancoDadosSingleton.getInstance().inserir("pokemonusuario", valores);

            //Adiciona 3 doces ao pokemon capturado
            somarDoces(pkmnAux,3);

            //cria objeto PokemonCapturado com informações vindas do objeto Aparecimento parâmetro
            PokemonCapturado pc = new PokemonCapturado();
            pc.setLatitude(aparecimento.getLatitude());
            pc.setLongitude(aparecimento.getLongitude());
            pc.setDtCaptura(dtCap);

            //verifica se lista de algum pokemon ainda não existe
            if(pokemons.get(pkmnAux) == null) {
                pokemons.put(pkmnAux, new ArrayList<PokemonCapturado>());
                Log.d("CAPTURA", "Pokemon novo");
            }else{
                Log.d("CAPTURA", "Pokemon conhecido");
            }

            //adiciona o pokemon na lista da sua especie
            pokemons.get(pkmnAux).add(pc);

            //TODO: subir a captura para o servidor web

            return true;

        }catch (Exception e){
            Log.e("CAPTURA", "ERRO: " + e.getMessage());
            return false;
        }
    }

    public int getQuantidadeCapturas(Pokemon pkmn, boolean pegarTrocados){
        if(pegarTrocados) {
            if (pokemons.containsKey(pkmn)) {
                return pokemons.get(pkmn).size();
            }
        } else {
            if(pkmn == null) return 0;
            int ans = 0;
            if (pokemons.containsKey(pkmn)) {
                for (PokemonCapturado capt: pokemons.get(pkmn)) {
                    if (capt.getEstaBloqueado() == 0)
                        ans++;
                }
                return ans;
            }
        }
        return 0;
    }

    public int getQuantidadeCapturas(Pokemon pkmn){
        return getQuantidadeCapturas(pkmn, true);
    }

    public void Chocar(Location location, int idOvo){
        try {
            Log.i("CHOCAR", "Chocando " + ControladoraFachadaSingleton.getInstance().getPokemonOvo(idOvo).getNome());


            Pokemon pkmnAux = ControladoraFachadaSingleton.getInstance().getPokemonOvo(idOvo);

            //Obtem timeStamp da captura
            Map<String, String> ts = TimeUtil.getHoraMinutoSegundoDiaMesAno();
            String dtCap = ts.get("dia") + "/" + ts.get("mes") + "/" + ts.get("ano") + " " + ts.get("hora") + ":" + ts.get("minuto") + ":" + ts.get("segundo");

            //Prepara valores para serem persistidos no banco
            ContentValues valores = new ContentValues();
            valores.put("login", this.login);
            valores.put("idPokemon", pkmnAux.getNumero());
            valores.put("dtCaptura", dtCap);
            valores.put("latitude", location.getLatitude());
            valores.put("longitude", location.getLongitude());
            valores.put("estaBloqueado",0);

            //Persiste captura no banco
            BancoDadosSingleton.getInstance().inserir("pokemonusuario", valores);

            //Adiciona 3 doces ao pokemon capturado
            somarDoces(pkmnAux,3);

            //cria objeto PokemonCapturado com informações vindas do objeto Aparecimento parâmetro
            PokemonCapturado pc = new PokemonCapturado();
            pc.setLatitude(location.getLatitude());
            pc.setLongitude(location.getLongitude());
            pc.setDtCaptura(dtCap);

            //verifica se lista de algum pokemon ainda não existe
            if (pokemons.get(pkmnAux) == null) {
                pokemons.put(pkmnAux, new ArrayList<PokemonCapturado>());
                Log.d("CAPTURA", "Pokemon novo");
            } else {
                Log.d("CAPTURA", "Pokemon conhecido");
            }

            //adiciona o pokemon na lista da sua especie
            pokemons.get(pkmnAux).add(pc);

            ControladoraFachadaSingleton.getInstance().aumentaXp("choca");   //atualiza XP do usuário ao chocar um ovo

        }catch (Exception e){
            Log.e("CHOCAR", "ERRO: " + e.getMessage());
        }

    }

}
