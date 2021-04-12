package com.example.paintapp.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class PaintView : View {

    var backgroungBitmap: Bitmap? = null
    var viewBitmap: Bitmap? = null
    var path: Path = Path()
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var canvas: Canvas? = null

    var brushSize: Int = 0
    var eraserSize: Int = 0
    var bgColour = Color.WHITE
    var mX: Float = 0f
    var mY: Float = 0f

    val lastAction = ArrayList<Bitmap>()


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        brushSize = 10
        eraserSize = 10

        paint.setColor(Color.BLACK)
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = brushSize.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        backgroungBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        viewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(viewBitmap!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(bgColour)
        canvas?.drawBitmap(backgroungBitmap!!, 0f, 0f, null)
        canvas?.drawBitmap(viewBitmap!!, 0f, 0f, null)
    }


    fun addLastAction(bitmap: Bitmap) {
        lastAction.add(bitmap)
    }

    fun returnLastAction() {
        if (lastAction.size > 0) {
            lastAction.removeAt(lastAction.size - 1)
            if (lastAction.size > 0) {
                viewBitmap = lastAction.get(lastAction.size - 1)
            } else {
                viewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            }
            canvas = Canvas(viewBitmap!!)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.getX()
        val y = event?.getY()
        when (event!!.action) {
            MotionEvent.ACTION_UP -> touchUp()
            MotionEvent.ACTION_DOWN -> touchStart(x, y)
            MotionEvent.ACTION_MOVE -> touchMove(x, y)
        }
        return true
    }

    private fun touchUp() {
        path.reset()
        addLastAction(getBitmap())
    }

    private fun touchMove(x: Float?, y: Float?) {
        val dx = Math.abs(x!!.minus(mX))
        val dy = Math.abs(y!!.minus(mY))

        if (dx >= 4 || dy >= 4) {
            path.quadTo(x, y, (x + mX) / 2, (y + mY) / 2)

            mX = x
            mY = y

            canvas?.drawPath(path, paint)
            invalidate()
        }

    }

    private fun touchStart(x: Float?, y: Float?) {
        path.moveTo(x!!, y!!)
        mX = x
        mY = y
    }


    /**
     * ========================================================================================
     */

    fun setBackgroundColour(colour: Int) {
        bgColour = colour
        invalidate()
    }

    fun setBrushSize1(size: Int) {
        brushSize = size
        paint.strokeWidth = size.toFloat()
        invalidate()
    }

    fun setBrushColour(colour: Int) {
        paint.setColor(colour)
    }

    fun setEraserSize1(size: Int) {
        eraserSize = size
        invalidate()
    }

    fun enableEraser() {
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
    }

    fun disableEraser() {
        paint.setXfermode(null)
        paint.setShader(null)
        paint.setMaskFilter(null)
    }

    fun getBitmap(): Bitmap {
        this.isDrawingCacheEnabled = true
        this.buildDrawingCache()

        val bitmap = Bitmap.createBitmap(this.getDrawingCache())

        this.isDrawingCacheEnabled = false

        return bitmap
    }


}