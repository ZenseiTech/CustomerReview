package ca.zenseitech.customerreview.forms;

import ca.zenseitech.customerreview.util.CourseWords;

import java.io.Serializable;


/**
 * @author Jorge Rodriguez
 */
public class CustomerReviewForm implements Serializable
{
	private Double rating;
	private String headline;
	private String comment;

	public Double getRating()
	{
		return rating;
	}

	public void setRating(Double rating)
	{
		this.rating = rating;
	}

	public String getHeadline()
	{
		return headline;
	}

	public void setHeadline(String headline)
	{
		this.headline = headline;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public boolean isRatingNegative() {
		return this.rating < 0;
	}

	public boolean hasCurseWord() {
		return CourseWords.isACourseWord(this.headline) || CourseWords.isACourseWord(this.comment);
	}
}
