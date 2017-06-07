package net.jplugin.ext.filesvr.svc;

/**
 *  缩略图实现，将图片(jpg、bmp、png、gif等等)真实的变成想要的大小
 */

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

/*******************************************************************************
 * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法
 * compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 */
public class PicCompressHelper {
	private File file = null; // 文件对象
	private String inputDir; // 输入图路径
	private String outputDir; // 输出图路径
	private String inputFileName; // 输入图文件名
	private String outputFileName; // 输出图文件名
	private int outputWidth = 100; // 默认输出图片宽
	private int outputHeight = 100; // 默认输出图片高
	private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	public PicCompressHelper() { // 初始化变量
		inputDir = "";
		outputDir = "";
		inputFileName = "";
		outputFileName = "";
		outputWidth = 100;
		outputHeight = 100;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setOutputWidth(int outputWidth) {
		this.outputWidth = outputWidth;
	}

	public void setOutputHeight(int outputHeight) {
		this.outputHeight = outputHeight;
	}

	public void setWidthAndHeight(int width, int height) {
		this.outputWidth = width;
		this.outputHeight = height;
	}

	/*
	 * 获得图片大小 传入参数 String path ：图片路径
	 */
	public long getPicSize(String path) {
		file = new File(path);
		return file.length();
	}

	// 图片处理
	public void compressPic() {
		try {
			// 获得源文件
			file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				throw new RuntimeException("文件不存在：" + inputDir + inputFileName);
			}
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				throw new RuntimeException(" can't read,retry!" + inputDir
						+ inputFileName);
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (this.proportion == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth ;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight ;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = outputWidth; // 输出的图片宽度
					newHeight = outputHeight; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = null;
				try {
					saveImage(tag, outputDir + outputFileName);
					// out = new FileOutputStream(outputDir + outputFileName);
					// // JPEGImageEncoder可适用于其他图片类型的转换
					// JPEGImageEncoder encoder =
					// JPEGCodec.createJPEGEncoder(out);
					// encoder.encode(tag);
				} finally {
					closeQuitely(out);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException("压缩失败：" + inputDir + inputFileName, ex);
		}
	}

	static void saveImage(BufferedImage dstImage, String dstName)
			throws IOException {
		String formatName = dstName.substring(dstName.lastIndexOf(".") + 1);
//		ImageIO.write(dstImage, /* "GIF" */formatName /* format desired */,
//				new File(dstName) /* target */);
		ImageIO.write(dstImage, /* "GIF" */"JPG" /* format desired */,
				new File(dstName) /* target */);
	}

	/**
	 * @param out
	 */
	private void closeQuitely(FileOutputStream s) {
		if (s != null) {
			try {
				s.close();
			} catch (Exception e) {
				ServiceFactory.getService(ILogService.class)
						.getLogger(PicCompressHelper.class.getName())
						.error("关闭流异常", e);
			}
		}
	}

	public static void compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName) {
		PicCompressHelper pch = new PicCompressHelper();
		// 输入图路径
		pch.inputDir = inputDir;
		// 输出图路径
		pch.outputDir = outputDir;
		// 输入图文件名
		pch.inputFileName = inputFileName;
		// 输出图文件名
		pch.outputFileName = outputFileName;
		pch.compressPic();
	}

	public static void compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int width, int height,
			boolean gp) {
		PicCompressHelper pch = new PicCompressHelper();
		// 输入图路径
		pch.inputDir = inputDir;
		// 输出图路径
		pch.outputDir = outputDir;
		// 输入图文件名
		pch.inputFileName = inputFileName;
		// 输出图文件名
		pch.outputFileName = outputFileName;
		// 设置图片长宽
		pch.setWidthAndHeight(width, height);
		// 是否是等比缩放 标记
		pch.proportion = gp;
		pch.compressPic();
	}
	
	public static void compressSelf(String filename,int w,int h) {
		String dir = new File(filename).getParentFile().getAbsolutePath()+"/";
		String fname = new File(filename).getName();
		PicCompressHelper.compressPic(dir, dir,fname, fname, w, h, true);
	}

	// main测试
	// compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
	public static void main(String[] arg) {
		PicCompressHelper mypic = new PicCompressHelper();
		System.out.println("输入的图片大小：" + mypic.getPicSize("e:\\a.jpg") / 1024
				+ "KB");
		int count = 0; // 记录全部图片压缩所用时间
		int start = (int) System.currentTimeMillis(); // 开始时间
		mypic.compressPic("e:\\", "e:\\", "a.jpg", "a1.jpg", 120, 120, true);
		int end = (int) System.currentTimeMillis(); // 结束时间
		int re = end - start; // 但图片生成处理时间
	}
	
	public static void cutAndResizeImage(String src, String dest, int x, int y, int w,
			int h,int destWidth,int destHeight,int htmlImgWidth){
		
		if (htmlImgWidth>0){
			//px和物理像素进行换算
			int imgRealWidth = getImageWidth(src);
			if (imgRealWidth<=0){
				throw new RuntimeException("Can't get img width");
			}
			double rate = imgRealWidth*1.0 /htmlImgWidth;
			x = (int) Math.round(x * rate);
			y = (int) Math.round(y * rate);
			w = (int) Math.round(w * rate);
			h = (int) Math.round(h * rate);
		}
		
		cutImage(src,dest,x,y,w,h);
		PicCompressHelper.compressSelf(dest, destWidth, destHeight);
	}
	
	private static int getImageWidth(String file){
		Image img;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// 判断图片格式是否正确
		return img.getWidth(null);
	}

	public static void cutImage(String src, String dest, int x, int y, int w,
			int h){
		Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = null;
		ImageInputStream iis = null;
		try {
			in = new FileInputStream(src);
			iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg", new File(dest));
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			try{
				if (in!=null) in.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
}
