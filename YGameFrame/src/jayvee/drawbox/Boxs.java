package jayvee.drawbox;

import android.annotation.SuppressLint;
import java.util.Random;

/**
 * ������˵����
 * @param iX
 * ���ӵĻ�ͼX����
 * @param iY
 * ���ӻ�ͼY����
 * @param iKind
 * ���ӵ�������
 * @param isCreated
 * �������Ƿ���Ҫ���Ƴ�����Ϊ��ʱ�����ƣ�����ʱ����
 * @author Jayvee
 * 
 */
public class Boxs {
	public int iX;
	public int iY;
	public int iKind;
	public int isCreated;
	
/**
 * ������Ĺ��캯��
 * @param fRate
 * ���ø����Ӳ����Ƴ����ĸ��ʣ�0��1֮��ĸ�������
 * @author Jayvee
 */
//	@SuppressLint("FloatMath")
	public Boxs(float fRate) {
		Random rd = new Random();
		// rd.nextInt(11);//����0��10�������
		isCreated = (int) Math.floor(rd.nextInt(11) / (10 * fRate));
		iX = 800;
		iY = 336;
		iKind = rd.nextInt(4);
	}
/**
 * �����ӵ�X�������Ӻ���
 * @param x
 * int�����ӻ�ͼԭ��X����ֵ
 * @author Jayvee
 */
	public void addX(int x) {
		this.iX += x;
	}
	/**
	 * �����ӵ�Y�������Ӻ���
	 * @param y
	 * int�����ӻ�ͼԭ��Y����ֵ
	 * @author Jayvee
	 */
	public void addY(int y) {
		this.iY += y;
	}

}
