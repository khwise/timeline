package com.smilegate.core.domain.preferences;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by kh.jin on 2020. 4. 26.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicDomain {

    private Long memberNo;

    private String coverImageUrl;

    private String introduce;
}
