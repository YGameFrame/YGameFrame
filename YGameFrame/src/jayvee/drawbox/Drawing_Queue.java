package jayvee.drawbox;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import fyy.ygame_frame.base.YABaseDomainView;
import fyy.ygame_frame.base.YDrawInformation;
import fyy.ygame_frame.util.YImageUtil;
import game.run_cow_run.R;

public class Drawing_Queue extends YABaseDomainView<Drawing_Data> {

	private int xoffset = 5;

	private int[] btmpBoxIds = new int[] { R.drawable.box_1, R.drawable.box_2,
			R.drawable.box_3, R.drawable.box_4, };
	private Bitmap[] btmpBoxs;

//	Queue<Boxs> dq = new LinkedList<Boxs>();
	public Drawing_Queue(Drawing_Data domainData) {
		super(domainData);
		// TODO Auto-generated constructor stub
		// �ڹ��캯�������������У�������10������
//		for (int queue_count = 0; queue_count < 10; queue_count++) {
			Boxs b = new Boxs(0.5f);
//			dq.add(b);}

		

	}

	public void onReceiveBroadcastMsg(int iMsgKey, Object objectDetailMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onLoadBitmaps(Resources resources, int iWidth, int iHeight, int[] iMapLayoutParams) {
		// TODO Auto-generated method stub
		int iSideLength;
		iSideLength = (int) (iWidth*0.06);
		btmpBoxs = YImageUtil.getBitmapArray(resources, btmpBoxIds);
		//btmpBoxs = YImageUtil.stretchImageArray(btmpBoxs, iTileSideLength*2);
		btmpBoxs = YImageUtil.stretchImageArray(btmpBoxs, iSideLength);
	}

	@Override
	protected void onDraw(Canvas canvas, YDrawInformation drawInformation) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Queue<Boxs> dq = (Queue<Boxs>)drawInformation.objExtra;
		Boxs tempbox = dq.peek();
		for (int i = 0; i < dq.size(); i++) {
			tempbox = dq.peek();
			if (dq.peek().isCreated != 0) {
				canvas.drawBitmap(btmpBoxs[dq.peek().iKind], dq.peek().iX,
						dq.peek().iY, null);
			}
			tempbox = dq.poll();
			dq.add(tempbox);// ����βԪ��ȡ�������������ͷ��
		}
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawText("��һ������", drawInformation.iArg1, drawInformation.iArg2, paint);
		
//		canvas.drawText("��һ������", 200, 300, null);
		


	}

	@Override
	protected void onRecycleBitmaps() {
		// TODO Auto-generated method stub
		YImageUtil.recycleBitmapArray(btmpBoxs);
	}

}
