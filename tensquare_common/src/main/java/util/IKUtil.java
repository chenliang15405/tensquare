package util;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * IK分词器工具类
 */
public class IKUtil {

    /**
     * 对内容进行分词
     *
     * @param content
     * @param splitChar
     * @return
     */
    public static String split(String content, String splitChar) throws IOException {
        StringReader reader = new StringReader(content);
        //通过ik分词器进行分期
        //最大粒度切分 true, 最小粒度切分 false-->会切分更细
        IKSegmenter ikSegmenter = new IKSegmenter(reader, true);

        Lexeme lexeme = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((lexeme = ikSegmenter.next()) != null) {
            stringBuilder.append(lexeme.getLexemeText() + splitChar);
        }

        return stringBuilder.toString();
    }

}
