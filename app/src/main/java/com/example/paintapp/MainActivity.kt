package com.example.paintapp

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paintapp.adapters.ToolsAdapter
import com.example.paintapp.databinding.ActivityMainBinding
import com.example.paintapp.models.ToolsItem
import com.example.paintapp.paint.PaintView
import com.example.paintapp.utils.ToolsEnum
import com.example.paintapp.viewmodel.MainActivityViewmodel
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import java.io.File
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewmodel: MainActivityViewmodel
    lateinit var paintView: PaintView
    val REQUEST_PERMISSION_CODE = 1001
    val PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        paintView = binding.paintView

        viewmodel = ViewModelProvider(this).get(MainActivityViewmodel::class.java)
        observeViewmodel()
        setUpRecyclerView()

    }

    private fun observeViewmodel() {
        viewmodel.onToolSelected.observe(this, Observer {
            binding.ivToolSelected.setImageResource(it.id)
            when(it){
                ToolsEnum.BRUSH ->{
                    paintView.disableEraser()
                }
                ToolsEnum.ERASER ->{
                    onEraserClicked()
                }
                ToolsEnum.COLOUR ->{
                  showColourPickerDialog(false)
                }
                ToolsEnum.BACKGROUND ->{
                    showColourPickerDialog(true)
                }
                ToolsEnum.RETURN ->{
                    paintView.returnLastAction()
                }
                ToolsEnum.BRUSHSIZE ->{
                    showSeekbar()
                }
            }
        })
    }

    private fun showColourPickerDialog(shouldSetBackground: Boolean) {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose Colour")
            .initialColor(Color.WHITE)
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(12)
            .setPositiveButton("Ok", object : ColorPickerClickListener{
                override fun onClick(
                    d: DialogInterface?,
                    lastSelectedColor: Int,
                    allColors: Array<out Int>?
                ) {
                    if(shouldSetBackground){
                        paintView.setBackgroundColour(lastSelectedColor)
                    }else{
                        paintView.setBrushColour(lastSelectedColor)
                    }
                }

            })
            .build()
            .show()
    }

    private fun showSeekbar() {
        binding.rvTools.visibility = View.GONE
        binding.seekbarLayout.visibility = View.VISIBLE
        val seekbarLayout = binding.seekbarLayout
        val btn_ok = seekbarLayout.findViewById<Button>(R.id.btn_ok)
        val seekbar = seekbarLayout.findViewById<SeekBar>(R.id.appCompatSeekBar)
        btn_ok.setOnClickListener {
            binding.rvTools.visibility = View.VISIBLE
            binding.seekbarLayout.visibility = View.GONE
           if(!btn_ok.text.equals("Ok")){
               paintView.setBrushSize1(btn_ok.text.toString().toInt())
               paintView.setEraserSize1(btn_ok.text.toString().toInt())
           }
        }

        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                btn_ok.setText("$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }

    private fun onEraserClicked() {
        paintView.enableEraser()
    }

    private fun setUpRecyclerView() {
        val adapter = ToolsAdapter(viewmodel)
        binding.rvTools.adapter = adapter
        adapter.submitList(setUpToolIcons())
    }

    private fun setUpToolIcons(): List<ToolsItem>{
        val toolsList = ArrayList<ToolsItem>()
        toolsList.add(ToolsItem(id = R.drawable.ic_baseline_brush_24, name = ToolsEnum.BRUSH.toolName))
        toolsList.add(ToolsItem(id = R.drawable.ic_eraser, name = ToolsEnum.ERASER.toolName))
        toolsList.add(ToolsItem(id = R.drawable.ic_baseline_color_lens_24, name = ToolsEnum.COLOUR.toolName))
        toolsList.add(ToolsItem(id = R.drawable.ic_baseline_filter_hdr_24, name = ToolsEnum.BACKGROUND.toolName))
        toolsList.add(ToolsItem(id = R.drawable.ic_baseline_keyboard_return_24, name = ToolsEnum.RETURN.toolName))
        toolsList.add(ToolsItem(id = R.drawable.ic_baseline_crop_24, name = ToolsEnum.BRUSHSIZE.toolName))
        return toolsList
    }

    // Triggered on Back Button Pressed in XML
    fun onClearAllButtonClicked(view: View) {

    }

    // Triggered on Download Button Pressed in XML
    fun onDownloadButtonClicked(view: View) {
       if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
       }else{
           saveBitmap()
       }
    }

    private fun saveBitmap() {
        val bitmap = paintView.getBitmap()
        val fileName = UUID.randomUUID().toString() +".png"
        val folder = File( getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.toURI())
    }

    // Triggered on Home Button Pressed in XML
    fun onHomeButtonClicked(view: View) {

    }

    // Triggered on Share Button Pressed in XML
    fun onShareButtonClicked(view: View) {

    }
}