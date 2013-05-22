package jayvee.cow;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

import jayvee.drawbox.Boxs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import fyy.ygame_frame.base.YABaseDomainLogic;
import fyy.ygame_frame.base.YDrawInformation.YDrawInfoForm;
import fyy.ygame_frame.base.YGameEnvironment;
import fyy.ygame_frame.base.broadcast.YIBroadcast;
import fyy.ygame_frame.extra.YITask;
import fyy.ygame_frame.extra.YRequest;
//import fyy.ygame_map.logic.YMapLogic;
//import fyy.ygame_map.view.YAMapView;

public class CowLogic extends YABaseDomainLogic<CowData> {

	//
	// private YMapLogic mapLogic;
	// private YAMapView mapView;
	private Canvas canvas;
	// �����궨��
	public static final int iRun = 0;
	public static final int iJump = 1;
	public static final int iHurt = 2;

	// �߼�����
	private int iCurX = 150;// �������λ�����ó�ʼֵ
	private int iCurY = 140;
	private int iCurViewX = 0;
	private int iCurViewY = 140;
	private int iLastX = iCurX;
	private int iLastY = iCurY;
	private int damege = 100;
	private int xoffset = 0;
	private int yoffset = 0;
	private int is_being_hurt = 0;

	private Queue<Boxs> dq;
	private volatile Boxs front_box = new Boxs(0.4f);

	// �߼��ж�ֵ�����м�һ��Ҫ��������true/false����ת��������������
	private boolean isSprite_Left_Direction = false;// �����Ƿ����󣬳�ʼֵΪ��
	public boolean iJumpAble; // �������Ŀɷ���Ծ�ź�
	private boolean isJumping = false;// ��Ծ�����������ڽ���
	private boolean isOnGround = false;
	private boolean isAttacking = false;
	private boolean isHurting = false;

	// ��������
	private int iPerTileLength = 100; // ���������ÿ���ͼ��Ŀ�ȣ����أ�
	private int iBodyLength = 100;// ����ͼ��������С
	private final double iWidthRate = 0.11f; // ����ͼƬ���߼�ԭ������ڻ�ͼԭ���X��ƫ�Ʊ���
	private final double iHeightRate = 0.8f; // ����ͼƬ���߼�ԭ������ڻ�ͼԭ���Y��ƫ�Ʊ���
	private float iG = 3f; // ������������ٶȳ���
	private float upV = 30f;// ��Ծ���ڸ�����ʩ�ӵ����ϵ��ٶ�
	private int xV = 0;
	private int iVirtualGround = 400;
	public int iState;

	// public CowLogic(CowData domainData, YMapLogic mapLogic, Queue<Boxs> dq) {
	// super(domainData);
	// // TODO Auto-generated constructor stub
	// // this.mapLogic = mapLogic;
	// this.dq = dq;
	// }

	public CowLogic(CowData domainData) {
		// TODO Auto-generated constructor stub
		super(domainData);
		// this.dq = dq;
	}

	public void onReceiveBroadcastMsg(int iMsgKey, Object objectDetailMsg) {
		// TODO Auto-generated method stub
		switch (iMsgKey) {
		case YGameEnvironment.BroadcastMsgKey.MSG_DOMAIN_VIEW_LAYOUTED:
			iBodyLength = CowView.iPerLength;
			iG = 3f / 214f * iBodyLength;
			upV = 30f / 214f * iBodyLength;
			Log.i("�㲥��Ϣ", "�յ���������Ϣ");
			break;
		case CowView.iMSG_Trans_Canvas:
			canvas = (Canvas) objectDetailMsg;
		default:
			break;
		}
	}

	@Override
	protected void onSendRequests(
			java.util.Map<Integer, fyy.ygame_frame.base.YABaseDomainLogic<?>> domainLogics) {
	};

	YITask drawtask = new YITask() {

		int count = 0;
		int period = 11;
		int jumpcount = 0;

		public void execute(YDrawInfoForm drawInfoForm,
				Map<Integer, YABaseDomainLogic<?>> domainLogics) {
			if (!isHurting) {

				if (isOnGround && !isJumping) {
					count = count % period;
					// drawInfoBuilder.iX = iCurViewX;
					// drawInfoBuilder.iY = iCurViewY;
					drawInfoForm.iPicIndex = count;
					drawInfoForm.iPicKind = CowView.iRunImg;
					count++;

				} else if (isOnGround && isJumping) {
					drawInfoForm.iPicIndex = 0;
					drawInfoForm.iPicKind = CowView.iJumpImg;
				} else
				// if (!isOnGround && isJumping) {
				// drawInfoBuilder.iPicIndex = 1;
				// drawInfoBuilder.iPicKind = CowView.iJumpImg;
				// } else
				{
					jumpcount = jumpcount % 1;
					drawInfoForm.iPicIndex = jumpcount + 2;
					drawInfoForm.iPicKind = CowLogic.iJump;
				}
			}else {
				drawInfoForm.iPicIndex = 1;
				drawInfoForm.iPicKind = CowLogic.iHurt;
				
			}
		}
	};

	YITask YTask = new YITask() {
		int count = 0;
		int fall_count = 0;
		private int iPeriod = 1;
		private final static int PERIOD = 2;

		public void execute(YDrawInfoForm drawInfoForm,
				Map<Integer, YABaseDomainLogic<?>> domainLogics) {
			// TODO Auto-generated method stub
			// if (PERIOD != (iPeriod = iPeriod % PERIOD + 1))
			// return;
			// iCurX +=mapLogic.getiOffsetX();
			// iCurViewX = iCurX - mapLogic.getiOffsetX();
			int iCurColumn = (int) Math
					.floor((iCurX + iBodyLength * iWidthRate));
			int iCurRow = (int) Math.floor((iCurY + iBodyLength * iHeightRate));
			// Paint paint = new Paint();
			// paint.setColor(Color.RED);
			// canvas.drawPoint(iCurColumn, iCurRow, paint);
			int[] para = new int[] { iCurColumn, iCurRow };
			broadcastDomain.send(339, para, CowLogic.this);
			// ////////////������������������������������������������������//
			// int ihowFarIsBarrierInMyBottom = mapLogic
			// .howFarIsBarrierInMyBottom(iCurColumn, iCurRow);
			// System.out.println("�ұ��ϰ���Զ��===="
			// + mapLogic.howFarIsBarrierInMyRight(iCurColumn, iCurRow));
			// System.out.println("��������Զ��===="
			// + mapLogic.howFarIsBarrierInMyBottom(iCurColumn, iCurRow));
			// if (isJumping) {
			// iCurY -= upV - iG * count;
			// count++;
			// isOnGround = false;
			// if ((upV - iG * count) <= 0)// �����׶ν���
			// {
			// count = 0;
			// isJumping = false;
			// }
			// } else if (ihowFarIsBarrierInMyBottom > 0)// ����״̬������
			// {
			// isOnGround = false;
			// if (ihowFarIsBarrierInMyBottom < 1.3 * mapLogic
			// .getTileSideLengthByPixels()
			// && (iG * fall_count) > (mapLogic
			// .getTileSideLengthByPixels() / 2)) {
			// iCurY += ihowFarIsBarrierInMyBottom;// �����ӽ�����
			// // System.out.println("daolezheyibu"+"======"+iG*fall_count);
			// } else {
			// iCurY += iG * fall_count;
			// fall_count++;
			// }
			// } else {
			// if (ihowFarIsBarrierInMyBottom < 0)
			// iCurY += ihowFarIsBarrierInMyBottom;// ��ֹС��ΧǶ��
			// isOnGround = true;
			// fall_count = 0;
			// }

			// if (isJumping)
			// {
			// iCurY -=5;
			// fall_count++;
			// if (fall_count>40)
			// isJumping = false;
			// }else if (mapLogic.howFarIsBarrierInMyBottom(iCurColumn,
			// iCurRow)<10)
			// {
			// isOnGround = true;
			//
			// }else iCurY+=5;
			// iCurViewY = iCurY + mapLogic.getiOffsetY();
			// drawInfoBuilder.iY = iCurViewY;

			if (isJumping) {
				iCurY -= upV - iG * count;
				count++;
				isOnGround = false;
				if ((upV - iG * count) <= 0)// �����׶ν���
				{
					count = 0;
					isJumping = false;

				}
			} else // ��������Ĵ���
			{
				// int howfar = mapLogic.howFarIsBarrierInMyBottom(iCurColumn,
				// iCurRow);
				if (iCurRow < iVirtualGround) {
					if (iCurRow > iVirtualGround - 20) {
						iCurY += iVirtualGround - iCurRow;
					} else {
						iCurY += iG * fall_count;
						fall_count++;
					}
				} else {
					isOnGround = true;
					fall_count = 0;
					if (iCurRow > iVirtualGround) {
						// iCurY -= iCurRow - iVirtualGround;// ��ֹС��Χ��Ƕ���ϰ�
						Log.i("��Ϸ��Ϣ", "���Ǽҵ�ţ��ײ���ˣ�");
						isHurting = true;
					}else isHurting = false;
				}
			}
			// if (iCurRow>)
			iCurViewY = iCurY;
			drawInfoForm.iY = iCurViewY;
			drawInfoForm.iX = iCurViewX;
		}
	};

	@Override
	protected YITask[] onSubmitCurrentTasks() {
		// TODO Auto-generated method stub
		// System.out.println(isOnGround);
		System.out.println(front_box.iX);
		return new YITask[] { drawtask, YTask };
	}

	@Override
	protected int onDealRequest(YRequest request) {
		// TODO Auto-generated method stub
		switch (request.iKey) {
		case CowLogic.iRun:

			break;
		case CowLogic.iJump:

			if (isOnGround) {
				isJumping = true;
			}

			break;
		case 789:
			front_box = (Boxs) request.objExtra;
			System.out.println(front_box.iX);
			if (front_box.iX > 10 && front_box.iX < 100
					&& front_box.isCreated != 0) {
				iVirtualGround = 336;
			} else
				iVirtualGround = 400;

		default:
			break;
		}
		return 0;
	}

}
