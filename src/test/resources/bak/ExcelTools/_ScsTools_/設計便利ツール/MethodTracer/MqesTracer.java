package murata.co.methodcacher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import murata.co.Constants;
import murata.co.util.DateUtils;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <pre>
 * "MockMessageQueueingEcoSystemPSC"��p�̃g���[�T�[.
 * MethodTracer�Ɠ��l�̃��O�o�͂ɉ����AmockMessageQueueingEcoSystemPSC ���Ăяo��
 * Assert�����o�͂��܂�.
 * </pre>
 * murata.co.Mesage
 * @author K.Miura
 * @version $Id$
 * @since JDK5.0
 */
public class MqesTracer extends MethodTracer {

    /** �o�̓t�@�C���T�t�B�b�N�X. */
    private static final String MQ_OUT_FILE_SUFFIX = ".txt";

    /** �o�̓t�@�C���v���t�B�b�N�X. */
    protected String mqOutfilePrefix = "mqTestSrc";

    /** ���b�Z�[�W���M�Ăяo����. */
    protected int messageSendCallCount = 0;

    /** ���O�Ώۃt�B�[���h. */
    protected List<String> excludeFields = new ArrayList<String>();

    /**
     * {@inheritDoc}
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // ���s�O�ɁuMQ�Ȃ�v�̓��ꏈ������B
        if (invocation.getThis().getClass().getSimpleName().indexOf("PSC") != -1
            && invocation.getMethod().getName().equals("execute")) {
            outputMqSourceTemplate(invocation);
        }
        // �e���\�b�h���ĂԁB���ؗp�̃��O�o��
        return super.invoke(invocation);
    }

    /**
     * mockMessageQueueingEcoSystemPSC ���Ăяo��Assert�����o�͂��܂�.
     * @param invocation
     */
    private void outputMqSourceTemplate(MethodInvocation invocation) {

        try {

            StringBuilder sb = new StringBuilder();

            // ���O���X�g����A���O�L�[�}�b�v���쐬
            HashMap<String, String> excludeFieldMap =
                new HashMap<String, String>();
            for (String baseValue : excludeFields) {
                String value = baseValue.toLowerCase();
                excludeFieldMap.put(value, value);
            }

            Object[] args = invocation.getArguments();
            boolean isGroup = false;
            if (args == null || args.length != 6) {
                // �O���[�v�łɑΉ�
                isGroup = true;
            }

            if (messageSendCallCount == 0) {
                sb.append("        // MQ���b�Z�[�W���M(���ʊm�F)");
                sb.append(CR);
                sb
                    .append("        List<Map<String, Object>> messages = mockMessageQueueingEcoSystemPSC.getMessages();");
                sb.append(CR);
                sb.append("        // �����m�F");
                sb.append(CR);
                sb.append("        assertEquals(1, messages.size());");
                sb.append(CR);
            }
            sb.append("        // �f�[�^���e�擾");
            sb.append(CR);
            sb.append("        Map<String, Object> message = messages.get(0);");
            sb.append(CR);
            sb.append("        assertEquals(\"");
            sb.append(args[0]);
            sb
                .append("\", message.get(MockMessageQueueingEcoSystemPSC.Key.MESSAGE_TERMINAL));");
            sb.append(CR);
            sb.append("        assertEquals(\"");
            sb.append(args[1]);
            sb
                .append("\", message.get(MockMessageQueueingEcoSystemPSC.Key.START_UP_PROGRAM));");
            sb.append(CR);
            sb.append("        assertEquals(new Long(");
            sb.append(args[2]);
            sb
                .append("), message.get(MockMessageQueueingEcoSystemPSC.Key.FORMAT_NO));");
            sb.append(CR);
            sb.append("        assertEquals(\"");
            sb.append(args[3]);
            sb
                .append("\", message.get(MockMessageQueueingEcoSystemPSC.Key.PRODUCT_ID));");
            sb.append(CR);
            sb.append("        assertEquals(\"");
            sb.append(args[4]);
            sb
                .append("\", message.get(MockMessageQueueingEcoSystemPSC.Key.USER_ID));");
            sb.append(CR);

            if (isGroup) {
                sb.append("        assertEquals(new Long(");
                sb.append(args[6]);
                sb
                    .append("), message.get(MockMessageQueueingEcoSystemPSC.Key.GROUP_NO));");
                sb.append(CR);
            }


            sb.append("        // ���b�Z�[�WVO�m�F");
            sb.append(CR);
            sb.append("        ");
            sb.append(CR);

            // data�v���p�e�B�̊m�F�\�[�X����
            Object data = args[5];

            String sendDataClassName = data.getClass().getSimpleName();
            sb.append("        ");
            sb.append(sendDataClassName);
            sb.append(" ");
            String paramName = "data";
            if (messageSendCallCount > 0) {
                paramName += (messageSendCallCount + 1);
            }
            sb.append(paramName);
            sb.append(" = (");
            sb.append(sendDataClassName);
            sb
                .append(") message.get(MockMessageQueueingEcoSystemPSC.Key.DATA);");
            sb.append(CR);

            Set<String> keys = PropertyUtils.describe(data).keySet();
            for (String key : keys) {
                // �ΏۊO�t�B�[���h����
                if (key.equals("class")) continue;
                if (excludeFieldMap.containsKey(key.toLowerCase())) continue;

                // �v���p�e�B�̏����擾
                Object value = PropertyUtils.getProperty(data, key);
                writeAssert(sb, value, paramName, key);
                sb.append(CR);
            }

            // �t�@�C���o��
            mqOutLog(sb.toString());
            messageSendCallCount++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sb
     * @param value
     * @param paramName
     * @param key
     */
    private void writeAssert(StringBuilder sb, Object value, String paramName,
        String key) {
        sb.append("        assertEquals(");
        writeStrValue(sb, value);
        sb.append(", ");
        if (value instanceof java.util.Date) {
            // Date �̂ݕϊ�
            sb.append("DateUtils.convertDateToString(");
            sb.append(paramName);
            sb.append(".get");
            String methodName =
                key.substring(0, 1).toUpperCase() + key.substring(1);
            sb.append(methodName);
            sb.append("()");
            sb.append(", Constants.DATE_YYYY_MM_DD)");
        } else {
            // Date�łȂ�
            sb.append(paramName);
            sb.append(".get");
            String methodName =
                key.substring(0, 1).toUpperCase() + key.substring(1);
            sb.append(methodName);
            sb.append("()");
        }
        sb.append(");");
    }

    /**
     * Object(�����^��String or ���b�p�[�^�̂݊���)�̒l��StringBuilder�ɏ����o���B
     * null�̏ꍇ�́A"null"�Əo�͂���B
     * @param sb
     * @param value
     */
    private void writeStrValue(StringBuilder sb, Object value) {
        if (value == null) {
            sb.append("null");
        } else {
            if (value instanceof Long) {
                sb.append("new Long(");
                sb.append(value.toString());
                sb.append(")");
            } else if (value instanceof BigDecimal) {
                sb.append("new BigDecimal(");
                sb.append(value.toString());
                sb.append(")");
            } else if (value instanceof java.util.Date) {
                Date dateValue = (Date)value;
                sb.append("\"");
                sb.append(DateUtils.convertDateToString(dateValue,
                    Constants.DATE_YYYY_MM_DD));
                sb.append("\"");
            } else {
                sb.append("\"");
                sb.append(value.toString());
                sb.append("\"");
            }
        }
    }

    /**
     * �o�͏����B
     * �؂�ւ�����悤�Ƀ��\�b�h�ɐ؂�o���܂��BLog�ɂł���Ȃ炻��ł��悵�B
     * @param message
     * @throws IOException
     */
    public void mqOutLog(String message) throws IOException {
        String outFilePath =
            (new File(eviOutputPath)).getPath() + File.separator
                + mqOutfilePrefix + fileNo + MQ_OUT_FILE_SUFFIX;
        FileWriter fw =
            new FileWriter(outFilePath, (messageSendCallCount != 0));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(message);
        bw.newLine();
        bw.close();
        fw.close();
    }

    public void initOutMessage(String message) {
        super.initOutMessage(message);
        messageSendCallCount = 0;
    }

    /**
     * mqOutfilePrefix��߂��B
     * <br>
     * @return  mqOutfilePrefix
     */
    public String getMqOutfilePrefix() {
        return mqOutfilePrefix;
    }

    /**
     * mqOutfilePrefix��ݒ肷��B
     * <br>
     * @param mqOutfilePrefix String
     */
    public void setMqOutfilePrefix(String mqOutfilePrefix) {
        this.mqOutfilePrefix = mqOutfilePrefix;
    }

    /**
     * excludeFields��߂��B
     * <br>
     * @return  excludeFields
     */
    public List<String> getExcludeFields() {
        return excludeFields;
    }

    /**
     * excludeFields��ݒ肷��B
     * <br>
     * @param excludeFields List<String>
     */
    public void setExcludeFields(List<String> excludeFields) {
        this.excludeFields = excludeFields;
    }

}
