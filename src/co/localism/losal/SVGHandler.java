package co.localism.losal;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class SVGHandler {

	public SVGHandler(){
		
	}
	
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @return
	 */
	public ImageView svg_to_imageview(Context ctx, int resId){
		return svg_to_imageview(ctx,resId, -1, -1, 1f);
	}
	
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @param opacity
	 * @return
	 */
	public ImageView svg_to_imageview(Context ctx, int resId, float opacity){
		return svg_to_imageview(ctx,resId, -1, -1, opacity);
	}
	
	/**
	 * 
	 * @param ctx
	 * @param resId 
	 * @param original_color
	 * @param desired_color
	 * @param opacity
	 * @return ImageView
	 */
	public ImageView svg_to_imageview(Context ctx, int resId, int original_color, int desired_color, float opacity) {
		ImageView iv = new ImageView(ctx);
		SVG svg;
		if(original_color == -1 || desired_color == -1)
   			 svg = SVGParser.getSVGFromResource(ctx.getResources(), resId);
       else
		svg = SVGParser.getSVGFromResource(ctx.getResources(), resId, ctx.getResources().getColor(original_color), ctx.getResources().getColor(desired_color));   
//		svg = SVGParser.getSVGFromResource(ctx.getResources(), resId, 0xFF9FBF3B, ctx.getResources().getColor(desired_color));   
		iv.setImageDrawable(svg.createPictureDrawable());
		iv.setAlpha(opacity);
        iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		return iv;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @param opacity
	 * @param width
	 * @param height
	 * @return
	 */
	public ImageView svg_to_imageview(Context ctx, int resId, float opacity, int width, int height){
		ImageView iv = svg_to_imageview(ctx, resId, -1, -1, opacity);
		LayoutParams params = new LayoutParams(width, height); 
        iv.setLayoutParams(params);
        return iv;
	}
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @param opacity
	 * @param width
	 * @param height
	 * @param original_color
	 * @param desired_color
	 * @return
	 */
	public ImageView svg_to_imageview(Context ctx, int resId, float opacity, int width, int height, int original_color, int desired_color){
		ImageView iv = svg_to_imageview(ctx, resId, original_color, desired_color, opacity);
		LayoutParams params = new LayoutParams(width, height); 
        iv.setLayoutParams(params);
        return iv;
	}
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @return
	 */
	public Drawable svg_to_drawable(Context ctx, int resId){
		return svg_to_drawable(ctx,resId, -1, -1);
	}
	
	
	/**
	 * 
	 * @param ctx
	 * @param resId
	 * @param original_color
	 * @param desired_color
	 * @return
	 */
	public Drawable svg_to_drawable(Context ctx, int resId, int original_color, int desired_color) {
		ImageView iv = new ImageView(ctx);
		SVG svg;
		if(original_color == -1 || desired_color == -1)
   			 svg = SVGParser.getSVGFromResource(ctx.getResources(), resId);
       else
		svg = SVGParser.getSVGFromResource(ctx.getResources(), resId, ctx.getResources().getColor(original_color), ctx.getResources().getColor(desired_color));   
		return svg.createPictureDrawable();
	}
	
	
	
}
