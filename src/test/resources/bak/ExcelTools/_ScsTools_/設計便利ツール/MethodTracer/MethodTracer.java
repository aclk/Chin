package murata.ps.bmc.psshippinginstructionsalesbmc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import murata.co.component.core.Cacheable;
import murata.co.core.io.XMLUtilsBean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ���ʃ��W���[���n���\�b�h��In��Out���L�^����g���[�T
 * @author K.Miura
 * @version $Id: MethodTracer.java 148745 2008-10-31 12:35:07Z A1S0MEM099 $
 * @since JDK5.0
 */
public class MethodTracer implements MethodInterceptor , Cacheable {

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
            sb.append((arg == null) ? "null" : arg.getClass().getName());
            sb.append(")");

            // �����̒l���o��
            if (arg instanceof String
                || arg instanceof Date
                || arg instanceof Long
                || arg instanceof BigDecimal
            ) {
                sb.append(CR);
                sb.append(arg.toString());
            }

            sb.append(CR);
            sb.append((arg == null) ? "null" : createObjectInfoString(arg));
        }
        outLog(sb.toString());

        // �o�ߎ��Ԃ��L�^����
        Date startTime = new Date();

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

        sb.append("���s���� : ");
        sb.append((new Date()).getTime() - startTime.getTime());
        sb.append("�~���b");
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

    /**
     *
     * {@inheritDoc}
     * @see murata.co.component.core.Cacheable#clear()
     */
    public void clear() {
        this.initOutMessage("���W���[���̃p�����[�^(IN,OUT)�̋L�^�J�n");
    }
}

/**
 * �I�u�W�F�N�g���̃v���p�e�B������͂���N���X
 * @author K.Miura
 */
class ObjectAnalayser {

    // �C���f���g�̐�
    private static int indentCount = 0;

    // ��������^�C�v
    private static final List<String> PASS_TYPES = new ArrayList<String>() {

        {
            super.add("class");
            super.add("pageKeyList");
            super.add("currentKey");
        }
    };

    /**
     * �f�t�H���g�R���X�g���N�^
     * @param arg0 ����������
     */
    private ObjectAnalayser() {
    }

    /**
     * ��͌ďo���\�b�h
     * <br />
     * �S�Ă̒l����͂��AString�^�̃��X�g�ňꊇ�ԋp���s��
     * @param obj �t�@�C���o�͂��s���I�u�W�F�N�g
     * @return ��͌���
     */
    public static List<String> analays(Object obj) {
        // �߂�l���X�g
        List<String> analysisList = null;
        try {
            // �C���f���g��������(static�̂��߁A�K�x�[�W�ΏۂɂȂ�Ȃ������ꍇ���l��)
            indentCount = 0;
            // �I�u�W�F�N�g�̉�͂��J�n����
            analysisList = analaysObject(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // ���ʂ�Ԃ�
        return analysisList;
    }

    /**
     * �o�̓I�u�W�F�N�g�̉�̓��\�b�h
     * <br />
     * �S�Ă̒l����͂��AString�^�̃��X�g�ňꊇ�ԋp���s��
     * @param obj �t�@�C���o�͂��s���I�u�W�F�N�g
     * @return �t�@�C���o�͓��e
     */
    private static List<String> analaysObject(Object obj) {

        // �ŏI�I�ɏo�͓��e���i�[����
        List<String> analaysResult = new ArrayList<String>();

        try {
            if (obj == null) {
                return null;
            }

            // List or Map �͏�����ύX����
            if (obj instanceof Collection) {
                List<String> result = analaysCollection((Collection)obj);
                analaysResult.addAll(result);
            } else if (obj instanceof Map) {
                List<String> result = analaysMap((Map)obj);
                analaysResult.addAll(result);
            } else if (XMLUtilsBean.SupportClasses.isSupport(obj)) {

            } else {
                // �l�`�F�b�N
                Set<Entry> entrys = PropertyUtils.describe(obj).entrySet();
                for (Entry entry : entrys) {
                    if (PASS_TYPES.contains((String)entry.getKey())) continue;
                    // �C���f���g����t������
                    // �o�͒l�𐶐�����
                    String message = getIndent() + entry.getKey() + " = ";
                    // �ċA�������f
                    if (isRecompare(entry.getValue())) {
                        // �ċA�I�Ɍďo��
                        analaysResult.add(message);
                        // �C���f���g�̃J�E���g�A�b�v
                        indentCount++;
                        analaysResult.addAll(analaysObject(entry.getValue()));
                    } else {
                        // �o�͒l�𐶐�����
                        // �P�ƃv���p�e�B�̂��߁A�o�͏��Ƃ���List�֓o�^
                        analaysResult.add(message + entry.getValue());
                    }
                }
            }
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }

        // �C���f���g���0���傫���ꍇ�͖߂�̍ۂɈ�߂�
        if (0 < indentCount) {
            indentCount--;
        }

        return analaysResult;
    }

    /**
     * �C���f���g�������ɃX�y�[�X��������쐬���ԋp����
     * @return �C���f���g������
     */
    private static String getIndent() {
        // �C���f���g�̐���������𐶐����ԋp����
        return StringUtils.repeat("    ", indentCount);
    }

    /**
     * �}�b�v�N���X����͂���
     * @param valueMap ��͂���}�b�v�N���X
     * @return ��͌���
     */
    private static List<String> analaysMap(Map valueMap) {
        // �V�K���X�g�𐶐�����
        List<String> analaysResult = new ArrayList<String>();
        // �l�`�F�b�N(Map�ݒ�Key��String�Œ�ł͂Ȃ�)
        Set<Entry> entrys = valueMap.entrySet();
        for (Entry entry : entrys) {
            // �ċA�������f
            if (isRecompare(entry.getValue())) {
                // �C���f���g����t������
                analaysResult.add(getIndent() + entry.getKey() + " = ");
                // �C���f���g�̃J�E���g�A�b�v
                indentCount++;
                analaysResult.addAll(analaysObject(entry.getValue()));
            } else {
                // �o�͒l�𐶐�����
                // �P�ƃv���p�e�B�̂��߁A�o�͏��Ƃ���List�֓o�^
                analaysResult.add(getIndent() + entry.getKey() + " = "
                    + entry.getValue());
            }
        }
        // ���ʂ�ԋp����
        return analaysResult;
    }

    /**
     * ���X�g�N���X����͂���B
     * @param values ��͂��郊�X�g�N���X
     * @return ��͌���
     */
    private static List<String> analaysCollection(Collection values) {
        // �v�f���J�E���g
        int i = 0;
        // �V�K���X�g�𐶐�����
        List<String> analaysResult = new ArrayList<String>();
        for (Object value : values) {
            String elementCount = "��" + (i++) + "�v�f"; // �J�E���g�A�b�v
            // �ċA�������f
            if (isRecompare(value)) {
                analaysResult.add(getIndent() + elementCount + " = ");
                // �C���f���g�̃J�E���g�A�b�v
                indentCount++;
                analaysResult.addAll(analaysObject(value));
            } else {
                // �C���f���g����t������
                // �P�ƃv���p�e�B�̂��߁A�o�͏��Ƃ���List�֓o�^
                analaysResult.add(getIndent() + elementCount + " = " + value);
            }
        }
        return analaysResult;
    }

    /**
     * �ċA�I�ɔ�r���\�b�h���Ăяo�����̔��f���\�b�h�B<br>
     * object���uList�v�A�uMap�v�̏ꍇ�A�ċA���������s���܂��B
     * @param result �l�o�̓I�u�W�F�N�g
     * @return true�F�ċA�����ΏہAfalse�F�ċA�����ΏۊO
     */
    private static boolean isRecompare(Object outputObj) {
        if (outputObj == null) {
            return false;
        } else if (outputObj instanceof Set || outputObj instanceof List
            || outputObj instanceof Map) {
            return true;
        } else if (outputObj.getClass().getSimpleName().endsWith("VO")) {
            // VO�͐ڔ����Ŕ��f����(�l�[�~���O���[���ɏ���)
            return true;
        } else if (outputObj.getClass().getPackage().getName()
            .indexOf("entity") > 0) {
            // entity��Package���̂��画�f���s�Ȃ�(�l�[�~���O���[���ɏ���(�p�b�P�[�W�\��))
            return true;
        }
        return false;
    }
}
