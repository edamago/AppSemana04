package pe.edu.idat.appsemana04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import pe.edu.idat.appsemana04.databinding.ActivityRegistroBinding
import pe.edu.idat.appsemana04.util.AppMensaje
import pe.edu.idat.appsemana04.util.TipoMensaje
import kotlin.collections.ArrayList

class RegistroActivity : AppCompatActivity(),View.OnClickListener,AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityRegistroBinding
    private var estadocivil = ""
    private val listaPersonas=ArrayList<String>()
    private val listaPreferencias=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarPersona.setOnClickListener(this)
        binding.btnListarPersonas.setOnClickListener(this)

        binding.spestadocivil.onItemSelectedListener=this

        binding.cbDeporte.setOnClickListener(this)
        binding.cbMusica.setOnClickListener(this)
        binding.cbOtros.setOnClickListener(this)

        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.estado_civil,
            android.R.layout.simple_spinner_item
        ).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spestadocivil.adapter=adapter
        }

    }

    fun validarNombresApellidos():Boolean
    {
        var respuesta=true

        if (binding.etNombre.text.toString().trim().isEmpty())
        {
            binding.etNombre.isFocusableInTouchMode=true
            binding.etNombre.requestFocus()
            respuesta=false
        }else if (binding.etApellido.text.toString().trim().isEmpty())
        {
            binding.etApellido.isFocusableInTouchMode=true
            binding.etApellido.requestFocus()
            respuesta=false
        }

        return respuesta
    }

    fun validarGenero():Boolean
    {
        var respuesta=true
        if(binding.rgGenero.checkedRadioButtonId==-1)
        {
            respuesta=false
        }

            return respuesta
    }

    fun validarPreferencias():Boolean
    {
        var respuesta=false

        if(binding.cbDeporte.isChecked || binding.cbMusica.isChecked || binding.cbMusica.isChecked)
            respuesta=true
        else respuesta=false

        return respuesta
    }
    fun validarEstadoCivil():Boolean
    {
        return estadocivil != ""
    }

    fun validarFormulario():Boolean
    {
        var respuesta=false
        var mensaje=""

        if(!validarNombresApellidos())
        {
            //mensaje="Ingrese nombres y apellidos"
            mensaje=getString(R.string.errornombreapellido)
        }else if(!validarGenero()){
            mensaje="Es obligatorio seleccionar el género"
        }else if(!validarEstadoCivil()){
            mensaje="Es obligatorio seleccionar el estado civil"
        }else if(!validarPreferencias()){
            mensaje="Debe seleccionar al menos una preferencia"
        }else {
            respuesta=true
        }

        if (!respuesta){
            AppMensaje.enviarMensaje(binding.root,mensaje,TipoMensaje.ERROR)
        }

        return respuesta;
    }

    override fun onClick(v: View?) {
        if(v!! is CheckBox){
            agregarQuitarPreferencia(v!!)
        }else {
            when(v!!.id){
                R.id.btnListarPersonas -> irListadoPersonas()
                R.id.btnRegistrarPersona->registrarPersona()
            }
        }

    }

    private fun agregarQuitarPreferencia(v: View) {
        val checkbox = v as CheckBox

        if(checkbox.isChecked)
            listaPreferencias.add(checkbox.text.toString())
        else
            listaPreferencias.remove(checkbox.text.toString())
    }

    private fun registrarPersona() {
        if(validarFormulario()){
            var infoPersona =   binding.etNombre.text.toString()+ " " +
                                binding.etApellido.text.toString()+ " "+
                                obtenerGenero()+" " +
                                listaPreferencias.toArray()+ " "+
                                estadocivil+" "+
                                binding.swNotificacion.isChecked

            listaPersonas.add(infoPersona)
            AppMensaje.enviarMensaje(binding.root,"Persona registrada correctamente",TipoMensaje.CORRECTO)
        }
    }

    fun obtenerGenero():String{
        return if(binding.rgGenero.checkedRadioButtonId.equals(R.id.rbMasculino))
                        binding.rbMasculino.text.toString()
                    else binding.rbFemenino.text.toString()
    }

    private fun irListadoPersonas() {
        var intentListado = Intent(applicationContext,ListaActivity::class.java).apply {
            putExtra("listaPersonas",listaPersonas)
        }
        startActivity(intentListado)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        estadocivil = if(position > 0)  parent!!.getItemAtPosition(position).toString() else ""
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}