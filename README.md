# solr_ansj
Integrate with ansj_seg for solr.
整合基础的ansj分词算法
并实现了若干filter。

配置样例如下：

  <fieldType name="text_ansj" class="solr.TextField" positionIncrementGap="10" autoGeneratePhraseQueries="false" >
     		<analyzer type="index">
      			<tokenizer class="ctst.AnsjIndexTokenizerFactory" conf="ansj.conf"/>
	   		<filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" enablePositionIncrements="true" />
	   		<filter class="ctst.filter.MergerFilterFactory" keepSingleChar="true"/>
	   		<filter class="solr.SynonymFilterFactory" synonyms="synonyms.txt" ignoreCase="true" expand="false"/>
	   		<filter class="ctst.filter.NumbricFilterFactory"/>
	   		<filter class="ctst.filter.MarkerFilterFactory"/>
	   		<filter class="ctst.filter.TzlsNGramFilterFactory" minGram="1"/>
	   		<!-- <filter class="ctst.filter.SingleCharFilterFactory"/>-->
     		</analyzer>
		<analyzer type="query">
       			<tokenizer class="ctst.AnsjQueryTokenizerFactory" conf="ansj.conf"/>
	   		<filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" enablePositionIncrements="true" />
	   		<filter class="ctst.filter.MergerFilterFactory" keepSingleChar="false"/>
	   		<filter class="ctst.filter.WeakFlagFilterFactory"/>
     		</analyzer>
</fieldType>
