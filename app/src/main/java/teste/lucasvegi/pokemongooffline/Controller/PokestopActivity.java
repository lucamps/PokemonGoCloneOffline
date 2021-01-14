package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.InteracaoPokestop;
import teste.lucasvegi.pokemongooffline.Model.Pokestop;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.Util.MyApp;

public class PokestopActivity extends Activity {
    private TextView placeName;
    private TextView placeInfo;
    private ImageView imgPokestopIcon;
    private Date tempoPkstop;
    private Pokestop Pkstp;
    private boolean Pegou = false;
    public String Portuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokestop);

        placeName = (TextView) findViewById(R.id.placeName);
        placeInfo = (TextView) findViewById(R.id.placeInfo);
        imgPokestopIcon = (ImageView) findViewById(R.id.imgPokestopIcon);
        //ContentValues valores = new ContentValues();
        //BancoDadosSingleton.getInstance().inserir("Pokestop",valores);
        Intent it = getIntent();
        Pokestop pokestop = (Pokestop) it.getSerializableExtra("pokestop");
        byte[] byteArray = it.getByteArrayExtra("foto");
        Pkstp = pokestop;
        Cursor cTradutor = BancoDadosSingleton.getInstance().buscar("traducao trad",
                new String[]{"trad.portugues portugues"},
                "trad.ingles = '" + pokestop.getDescri() + "'",
                "");
        if (cTradutor.getCount()>0) {
            while (cTradutor.moveToNext()) {
                int coluna = cTradutor.getColumnIndex("portugues");
                Portuga = cTradutor.getString(coluna);
            }
        } else {
            Portuga = " ";
        }
        placeName.setText(pokestop.getNome());
        placeInfo.setText(Portuga);
        if(byteArray != null)
            imgPokestopIcon.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        tempoPkstop = pokestop.getUltimoAcesso();
        cTradutor.close();
    }

    public void clickReturnBtn(View btnReturn){
        Intent it = new Intent(this, MapActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra("tempo", tempoPkstop);
        it.putExtra("pokestop", Pkstp);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MapActivity.class);
        it.putExtra("tempo", tempoPkstop);
        it.putExtra("pokestop", Pkstp);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);
        finish();
    }

    public void PegaOvo(View view) {
        Pegou = false;
        Date TempoAtual = Calendar.getInstance().getTime();

        InteracaoPokestop inter = ControladoraFachadaSingleton.getInstance().getUltimaInteracao(Pkstp);

        if(inter.getUltimoAcesso() == null){
            ControladoraFachadaSingleton.getInstance().interagePokestop(Pkstp, TempoAtual);
            Pegou = true;
        }
        else{
            double diff = TempoAtual.getTime() - inter.getUltimoAcesso().getTime();
            int diffSec = (int)diff/1000;
            if(diffSec > 300){

                ControladoraFachadaSingleton.getInstance().interagePokestop(Pkstp, TempoAtual);

                //Pega o ovo
                Pegou = true;
            }
            else{
                Toast toastEspere = Toast.makeText(MyApp.getAppContext(),"Espere mais "+ String.valueOf(300-diffSec) +" segundos",Toast.LENGTH_SHORT);
                toastEspere.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,0);
                toastEspere.show();
            }

        }

        if(Pegou) {
            Integer xp = ControladoraFachadaSingleton.getInstance().getXpEvento("pokestop");
            Integer ovos = ControladoraFachadaSingleton.getInstance().getOvos().size();
            if(ovos > 8){
                Toast toastInventario = Toast.makeText(MyApp.getAppContext(), "Inventário de ovos está cheio!", Toast.LENGTH_LONG);
                toastInventario.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,0);
                toastInventario.show();
            }
            else{
                Toast toastOvo = Toast.makeText(MyApp.getAppContext(), "Pegou Ovo ", Toast.LENGTH_LONG);
                toastOvo.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,0);
                toastOvo.show();
            }

        }
    }

}

