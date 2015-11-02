package com.tzls.search.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class AnsjQueryTokenizer extends ChineseTokenizer {
	private boolean removePunc;

	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;
	
	int lastOffset = 0;
	int endPosition = 0;
	private Iterator<Term> tokenIter;
	private List<Term> tokenBuffer;


	public AnsjQueryTokenizer(Reader input,boolean removePunc) {
		super(input);
		offsetAtt = addAttribute(OffsetAttribute.class);
		termAtt = addAttribute(CharTermAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		this.removePunc = removePunc;
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (tokenIter == null || !tokenIter.hasNext()) {
			String currentSentence = checkSentences();
			if (currentSentence != null) {
				tokenBuffer = new ArrayList<Term>();
				for (Term term : ToAnalysis.parse(currentSentence)) {
					if("".equals(term.getName().trim())){
						continue;
					}
					if(SPACES.contains(term.getName())){
						continue;
					}
					if (removePunc && stopwords.contains(term.getName())){
						continue;
					}
					tokenBuffer.add(term);
				}

				tokenIter = tokenBuffer.iterator();
				if (!tokenIter.hasNext()) {
					return false;
				}
			} else {
				return false; // no more sentences, end of stream!
			}
		}
		clearAttributes();

		Term term = tokenIter.next();
		if (removePunc) {
			while (stopwords.contains(term.getName())) {
				if (!tokenIter.hasNext()) {
				} else {
					term = tokenIter.next();
				}
			}
		}
		termAtt.append(term.getName());
		termAtt.setLength(term.getName().length());

		int currentStart = tokenStart + term.getOffe();
		int currentEnd = tokenStart + term.toValue();
		offsetAtt.setOffset(currentStart, currentEnd);
		if(term.getNatureStr()!=null){
			typeAtt.setType(term.getNatureStr());
		}else{
			typeAtt.setType("canda");
		}

		// int pi = currentStart - lastOffset;
		// if(term.getOffe() <= 0) {
		// pi = 1;
		// }
		// positionIncrementAtt.setPositionIncrement( pi );
		lastOffset = currentStart;
		endPosition = currentEnd;
		return true;
	}

	@Override
	public void reset() throws IOException {
		super.reset();
	}

	public final void end() {
		// set final offset
		int finalOffset = correctOffset(this.endPosition);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

}
