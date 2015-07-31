package murata.co.btpremote2bxp;

import murata.co.application.BasePGM;
import murata.co.application.Result;
import murata.co.btpremote.behavior.Behavior;
import murata.co.mqes.vo.RemoteVO;
import murata.co.vo.BatchCommonVO;

/**
 * BtpRemote��Behavior�N���X��Bxp��PGM�Ɍ���������A�_�v�^�N���X�B
 * <br>
 * @author K.Miura
 * @version $Id:
 * @since JDK5.0
 */
public class BtpRemote2BxpAdapter extends BasePGM {

    /** Behavior */
    private Behavior<RemoteVO> behavior;

    /** Behavior�ɑ��荞��VO */
    private RemoteVO remoteVo;

    /**
     * ���s�����B
     * �����ABehavior�I�u�W�F�N�g��execute��@���B
     * <br />
     * {@inheritDoc}
     * @see murata.co.application.Executable#execute(murata.co.vo.BatchCommonVO)
     */
    public Result execute(BatchCommonVO dummyVo) {
        // behavior�I�u�W�F�N�g�����s�B
        this.behavior.execute(this.remoteVo);
        return new Result();
    }

    /**
     * behavior��ݒ肷��B
     * <br>
     * @param behavior Behavior<RemoteVO>
     */
    public void setBehavior(Behavior<RemoteVO> behavior) {
        this.behavior = behavior;
    }

    /**
     * remoteVo��ݒ肷��B
     * <br>
     * @param remoteVo RemoteVO
     */
    public void setRemoteVo(RemoteVO remoteVo) {
        this.remoteVo = remoteVo;
    }

}
