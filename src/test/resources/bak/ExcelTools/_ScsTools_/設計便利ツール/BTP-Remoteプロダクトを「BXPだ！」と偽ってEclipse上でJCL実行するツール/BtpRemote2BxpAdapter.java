package murata.co.btpremote2bxp;

import murata.co.application.BasePGM;
import murata.co.application.Result;
import murata.co.btpremote.behavior.Behavior;
import murata.co.mqes.vo.RemoteVO;
import murata.co.vo.BatchCommonVO;

/**
 * BtpRemoteのBehaviorクラスをBxpのPGMに見せかけるアダプタクラス。
 * <br>
 * @author K.Miura
 * @version $Id:
 * @since JDK5.0
 */
public class BtpRemote2BxpAdapter extends BasePGM {

    /** Behavior */
    private Behavior<RemoteVO> behavior;

    /** Behaviorに送り込むVO */
    private RemoteVO remoteVo;

    /**
     * 実行処理。
     * 内部、Behaviorオブジェクトのexecuteを叩く。
     * <br />
     * {@inheritDoc}
     * @see murata.co.application.Executable#execute(murata.co.vo.BatchCommonVO)
     */
    public Result execute(BatchCommonVO dummyVo) {
        // behaviorオブジェクトを実行。
        this.behavior.execute(this.remoteVo);
        return new Result();
    }

    /**
     * behaviorを設定する。
     * <br>
     * @param behavior Behavior<RemoteVO>
     */
    public void setBehavior(Behavior<RemoteVO> behavior) {
        this.behavior = behavior;
    }

    /**
     * remoteVoを設定する。
     * <br>
     * @param remoteVo RemoteVO
     */
    public void setRemoteVo(RemoteVO remoteVo) {
        this.remoteVo = remoteVo;
    }

}
