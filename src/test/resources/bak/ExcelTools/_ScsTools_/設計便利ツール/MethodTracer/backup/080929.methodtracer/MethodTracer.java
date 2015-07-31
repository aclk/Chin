package murata.ss.sz.szo0040.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import murata.co.test.producttest.testcase.ObjectAnalayser;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * ���ʃ��W���[���n���\�b�h��In��Out���L�^����g���[�T
 * @author K.Miura
 * @version $Id: MethodTracer.java 68504 2008-04-15 10:29:44Z A1B5MEMB $
 * @since JDK5.0
 */
public class MethodTracer implements MethodInterceptor {

    /** �o�̓t�@�C�����̃v���t�B�b�N�X�̏����l */
    protected static final String OUT_FILE_PREFIX = "methodTraceInAndOut";

    /** �o�̓t�@�C�����̃T�C�t�B�b�N�X(�g���q)�̏����l */
    protected static final String OUT_FILE_SYFIX = ".log";

    /** �V�X�e�����Ƃ̉��s���� */
    protected static final String CR = System.getProperty("line.separator");

    /** �g���[�X���ʂ̏o�̓p�X */
    protected String eviOutputPath;

    /** �o�̓t�@�C�����̃v���t�B�b�N�X */
    protected String outfilePrefix = OUT_FILE_PREFIX;

    // ���O�̏����͉��f�I�Ȋ֐S���ł�
//    private static final Log log = LogFactory.getLog(MethodTracer.class);

    /** �Ăяo���� */
    protected long callCount = 0;
    /** �t�@�C���̔ԍ�(�����ڂ�f���o���Ă��邩) */
    protected long fileNo = 0;

    /**
     * {@inheritDoc}
     * @throws Throwable
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        StringBuilder sb = new StringBuilder();
        Object ret = null;

        // �������o��
        String logHeader = "No" + (++callCount) + ":" + invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName() + "() �����s�B";
        sb.append(logHeader);
        int i = 0;
        for (Object arg : invocation.getArguments()){
            sb.append(CR);
            sb.append("����");
            sb.append(++i);
            sb.append(" : (�^��-");
            sb.append(arg.getClass().getName());
            sb.append(")");
            sb.append(CR);
            sb.append((arg == null) ? "null" : createObjectInfoString(arg));
        }
        outLog(sb.toString());

        try {
            // ���\�b�h�����s���A���ʂ�ێ�
            ret = invocation.proceed();

        } catch (Exception e) {
            sb = new StringBuilder();
            sb.append(logHeader);
            sb.append(CR);
            sb.append("�߂�l : ��Exception���������Ă��܂��B�߂�l�s���B");
            sb.append(CR);
            outLog(sb.toString());
            throw e;
        }

        // �߂�l���o��
        sb = new StringBuilder();
        sb.append(logHeader);
        sb.append(CR);
        sb.append("�߂�l : (�^��-");
        if (ret == null) {
            sb.append("null");
            sb.append(")\n");
        } else {
            sb.append(ret.getClass().getName());
            sb.append(")");
            sb.append(CR);
            if (ret instanceof String
                || ret instanceof Date
                || ret instanceof Long
                || ret instanceof BigDecimal
            ) {
                sb.append(ret.toString());
            } else {
                sb.append(createObjectInfoString(ret));
            }
        }

        outLog(sb.toString());
        return ret;
    }

    /**
     * �I�u�W�F�N�g����͂��v���p�e�B�Ȃǂ̏��𕶎���\���Ŏ擾<br>
     * (�e�X�gFW�n�̌���VO�o�̓��e�B���e�B��q��)
     * @param vo ��͂���I�u�W�F�N�g
     * @return ��͌��ʂ̕�����\��
     */
    public String createObjectInfoString(Object vo) {
        StringBuilder sb = new StringBuilder();
        List<String> analysisList = ObjectAnalayser.analays(vo);
        if (analysisList !=null) {
            // ������擾�E�A������
            for (String outputValue : analysisList) {
                sb.append(outputValue);
                sb.append(CR);
            }
        }
        return sb.toString();
    }

    /**
     * �o�͏����B
     * �؂�ւ�����悤�Ƀ��\�b�h�ɐ؂�o���܂��BLog�ɂł���Ȃ炻��ł��悵�B
     * @param message
     * @param isAppend
     */
    public void outLog(String message, boolean isAppend) {
        // log.info(message);
        try {
            String outFilePath = (new File(eviOutputPath)).getPath() + File.separator + outfilePrefix + fileNo + OUT_FILE_SYFIX;
            FileWriter fw = new FileWriter(outFilePath , isAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(message);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {}
    }

    public void outLog(String message) {
        outLog(message , true);
    }


    public void initOutMessage(String message) {
        fileNo++;
        callCount = 0;
        outLog(message, false);
    }


    /**
     * eviOutputPath��߂��B
     * <br>
     * @return  eviOutputPath
     */
    public String getEviOutputPath() {
        return eviOutputPath;
    }


    /**
     * eviOutputPath��ݒ肷��B
     * <br>
     * @param eviOutputPath String
     */
    public void setEviOutputPath(String eviOutputPath) {
        this.eviOutputPath = eviOutputPath;
    }


    /**
     * outfilePrefix��߂��B
     * <br>
     * @return  outfilePrefix
     */
    public String getOutfilePrefix() {
        return outfilePrefix;
    }


    /**
     * outfilePrefix��ݒ肷��B
     * <br>
     * @param outfilePrefix String
     */
    public void setOutfilePrefix(String outfilePrefix) {
        this.outfilePrefix = outfilePrefix;
    }
}
